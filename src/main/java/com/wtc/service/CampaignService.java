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
    private final CampaignRecipientRepository recipientRepo;
    private final FirebaseMessagingService firebase;

    public CampaignResponse create(CampaignRequest req, WtcUser operator) {
        Segment segment = segmentRepo.findById(req.getSegmentId())
            .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
        Campaign c = Campaign.builder()
            .title(req.getTitle()).body(req.getBody()).type(req.getType())
            .segment(segment).createdBy(operator)
            .deeplink(req.getDeeplink()).status(CampaignStatus.DRAFT)
            .build();
        return toResponse(campaignRepo.save(c));
    }

    public CampaignResponse sendNow(Long campaignId) {
        Campaign c = campaignRepo.findById(campaignId)
            .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));
        if (c.getStatus() == CampaignStatus.SENT)
            throw new RuntimeException("Campanha já enviada");

        List<Client> clients = clientRepo.findBySegmentId(c.getSegment().getId());
        List<String> tokens = clients.stream()
            .map(cl -> cl.getUser().getFcmToken())
            .filter(t -> t != null && !t.isBlank()).toList();

        // Criar registros de destinatário
        clients.forEach(cl -> {
            CampaignRecipient rec = CampaignRecipient.builder()
                .campaign(c).client(cl).status(RecipientStatus.PENDING).build();
            recipientRepo.save(rec);
        });

        // Enviar push
        Map<String, String> data = Map.of(
            "campaignId", c.getId().toString(),
            "type", c.getType().name(),
            "deeplink", c.getDeeplink() != null ? c.getDeeplink() : ""
        );
        firebase.sendToMultipleTokens(tokens, c.getTitle(), c.getBody(), data);

        // Atualizar status
        clients.forEach(cl -> {
            recipientRepo.findByCampaignIdAndClientId(c.getId(), cl.getId()).ifPresent(rec -> {
                rec.setStatus(RecipientStatus.SENT);
                rec.setSentAt(LocalDateTime.now());
                recipientRepo.save(rec);
            });
        });

        c.setStatus(CampaignStatus.SENT);
        c.setSentAt(LocalDateTime.now());
        log.info("Campanha {} enviada para {} destinatários", campaignId, tokens.size());
        return toResponse(campaignRepo.save(c));
    }

    public List<CampaignResponse> findAll() {
        return campaignRepo.findAll().stream().map(this::toResponse).toList();
    }

    public CampaignResponse findById(Long id) {
        return toResponse(campaignRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Campanha não encontrada")));
    }

    private CampaignResponse toResponse(Campaign c) {
        CampaignResponse r = new CampaignResponse();
        r.setId(c.getId()); r.setTitle(c.getTitle()); r.setBody(c.getBody());
        r.setType(c.getType()); r.setStatus(c.getStatus()); r.setDeeplink(c.getDeeplink());
        r.setSegmentName(c.getSegment().getName());
        r.setSentAt(c.getSentAt()); r.setCreatedAt(c.getCreatedAt());
        return r;
    }
}
