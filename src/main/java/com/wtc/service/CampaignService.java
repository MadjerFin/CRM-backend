package com.wtc.service;

import com.wtc.dto.request.CampaignRequest;
import com.wtc.dto.response.CampaignResponse;
import com.wtc.entity.*;
import com.wtc.enums.*;
import com.wtc.firebase.FirebaseMessagingService;
import com.wtc.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {

    private final CampaignRepository campaignRepo;
    private final SegmentRepository segmentRepo;
    private final ClientRepository clientRepo;
    private final WtcUserRepository userRepo;
    private final CampaignRecipientRepository recipientRepo;
    private final FirebaseMessagingService firebase;

    public CampaignResponse create(CampaignRequest req, WtcUser operator) {
        Segment segment = segmentRepo.findById(req.getSegmentId())
            .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
        Campaign c = Campaign.builder()
            .title(req.getTitle()).body(req.getBody())
            .type(req.getType() != null ? req.getType() : CampaignType.PROMO)
            .segmentId(segment.getId()).segmentName(segment.getName())
            .createdById(operator.getId())
            .deeplink(req.getDeeplink()).status(CampaignStatus.DRAFT)
            .build();
        return toResponse(campaignRepo.save(c));
    }

    public CampaignResponse sendNow(String campaignId) {
        Campaign c = campaignRepo.findById(campaignId)
            .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));
        if (c.getStatus() == CampaignStatus.SENT)
            throw new RuntimeException("Campanha já enviada");

        List<Client> clients = clientRepo.findBySegmentId(c.getSegmentId());
        List<String> tokens = clients.stream()
            .map(cl -> userRepo.findById(cl.getUserId())
                .map(WtcUser::getFcmToken).orElse(null))
            .filter(t -> t != null && !t.isBlank()).toList();

        clients.forEach(cl -> recipientRepo.save(
            CampaignRecipient.builder()
                .campaignId(c.getId()).clientId(cl.getId())
                .status(RecipientStatus.PENDING).build()));

        Map<String, String> data = Map.of(
            "campaignId", c.getId(), "type", c.getType().name(),
            "deeplink", c.getDeeplink() != null ? c.getDeeplink() : "");
        firebase.sendToMultipleTokens(tokens, c.getTitle(), c.getBody(), data);

        clients.forEach(cl ->
            recipientRepo.findByCampaignIdAndClientId(c.getId(), cl.getId()).ifPresent(rec -> {
                rec.setStatus(RecipientStatus.SENT);
                rec.setSentAt(LocalDateTime.now());
                recipientRepo.save(rec);
            }));

        c.setStatus(CampaignStatus.SENT);
        c.setSentAt(LocalDateTime.now());
        log.info("Campanha {} enviada para {} destinatários", campaignId, tokens.size());
        return toResponse(campaignRepo.save(c));
    }

    public List<CampaignResponse> findAll() {
        return campaignRepo.findAll().stream().map(this::toResponse).toList();
    }

    public CampaignResponse findById(String id) {
        return toResponse(campaignRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Campanha não encontrada")));
    }

    private CampaignResponse toResponse(Campaign c) {
        return CampaignResponse.builder()
            .id(c.getId()).title(c.getTitle()).body(c.getBody())
            .type(c.getType()).status(c.getStatus()).deeplink(c.getDeeplink())
            .segmentName(c.getSegmentName())
            .sentAt(c.getSentAt()).createdAt(c.getCreatedAt())
            .build();
    }
}
