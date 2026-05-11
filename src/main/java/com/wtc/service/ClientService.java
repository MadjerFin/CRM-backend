package com.wtc.service;

import com.wtc.dto.request.AnnotationRequest;
import com.wtc.dto.request.ClientRequest;
import com.wtc.dto.response.*;
import com.wtc.entity.*;
import com.wtc.enums.ClientStatus;
import com.wtc.enums.TaskStatus;
import com.wtc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepo;
    private final WtcUserRepository userRepo;
    private final SegmentRepository segmentRepo;
    private final AnnotationRepository annotationRepo;
    private final MessageRepository messageRepo;
    private final CampaignRecipientRepository campRecipRepo;
    private final TaskRepository taskRepo;

    public ClientResponse create(ClientRequest req) {
        WtcUser user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Client c = Client.builder()
            .user(user)
            .tags(req.getTags())
            .score(req.getScore())
            .status(req.getStatus() != null ? req.getStatus() : ClientStatus.ACTIVE)
            .phone(req.getPhone())
            .company(req.getCompany())
            .employeeCount(req.getEmployeeCount())
            .sector(req.getSector())
            .companyLevel(req.getCompanyLevel())
            .website(req.getWebsite())
            .city(req.getCity())
            .state(req.getState())
            .build();
        if (req.getSegmentId() != null)
            c.setSegment(segmentRepo.findById(req.getSegmentId()).orElse(null));
        return toClientResponse(clientRepo.save(c));
    }

    public ClientResponse update(Long id, ClientRequest req) {
        Client c = clientRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (req.getSegmentId() != null)
            c.setSegment(segmentRepo.findById(req.getSegmentId()).orElse(null));
        if (req.getTags()          != null) c.setTags(req.getTags());
        if (req.getScore()         != null) c.setScore(req.getScore());
        if (req.getStatus()        != null) c.setStatus(req.getStatus());
        if (req.getPhone()         != null) c.setPhone(req.getPhone());
        if (req.getCompany()       != null) c.setCompany(req.getCompany());
        if (req.getEmployeeCount() != null) c.setEmployeeCount(req.getEmployeeCount());
        if (req.getSector()        != null) c.setSector(req.getSector());
        if (req.getCompanyLevel()  != null) c.setCompanyLevel(req.getCompanyLevel());
        if (req.getWebsite()       != null) c.setWebsite(req.getWebsite());
        if (req.getCity()          != null) c.setCity(req.getCity());
        if (req.getState()         != null) c.setState(req.getState());
        return toClientResponse(clientRepo.save(c));
    }

    public List<ClientResponse> findWithFilters(ClientStatus status, Long segmentId, String tag) {
        return clientRepo.findWithFilters(status, segmentId, tag)
            .stream().map(this::toClientResponse).toList();
    }

    public List<ClientResponse> search(String q) {
        return clientRepo.search(q).stream().map(this::toClientResponse).toList();
    }

    public ClientResponse findByIdAsResponse(Long id) {
        return toClientResponse(clientRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado")));
    }

    public Client findById(Long id) {
        return clientRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public void addAnnotation(Long clientId, AnnotationRequest req, WtcUser operator) {
        Client c = findById(clientId);
        annotationRepo.save(
            Annotation.builder().client(c).operator(operator).content(req.getContent()).build());
    }

    public Profile360Response getProfile360(Long clientId) {
        Client client = findById(clientId);
        Long userId = client.getUser().getId();

        Profile360Response p = new Profile360Response();
        p.setClient(toClientResponse(client));

        p.setLastMessages(messageRepo.findConversation(userId).stream()
            .limit(10).map(this::toMessageResponse).toList());

        p.setLastCampaigns(campRecipRepo.findByClientId(clientId).stream()
            .limit(5).map(r -> {
                var cr = new CampaignResponse();
                cr.setId(r.getCampaign().getId());
                cr.setTitle(r.getCampaign().getTitle());
                cr.setStatus(r.getCampaign().getStatus());
                cr.setSentAt(r.getCampaign().getSentAt());
                return cr;
            }).toList());

        p.setOpenTasks(taskRepo.findByAssignedToIdAndStatus(userId, TaskStatus.OPEN)
            .stream().map(Task::getTitle).toList());

        p.setAnnotations(annotationRepo.findByClientIdOrderByCreatedAtDesc(clientId)
            .stream().limit(5).map(a -> a.getOperator().getName() + ": " + a.getContent()).toList());

        return p;
    }

    public ClientResponse toClientResponse(Client c) {
        return ClientResponse.builder()
            .id(c.getId())
            .userId(c.getUser().getId())
            .name(c.getUser().getName())
            .email(c.getUser().getEmail())
            .segmentId(c.getSegment() != null ? c.getSegment().getId() : null)
            .segmentName(c.getSegment() != null ? c.getSegment().getName() : null)
            .tags(c.getTags())
            .score(c.getScore())
            .status(c.getStatus())
            .phone(c.getPhone())
            .company(c.getCompany())
            .employeeCount(c.getEmployeeCount())
            .sector(c.getSector())
            .companyLevel(c.getCompanyLevel())
            .website(c.getWebsite())
            .city(c.getCity())
            .state(c.getState())
            .createdAt(c.getCreatedAt())
            .build();
    }

    private MessageResponse toMessageResponse(Message m) {
        MessageResponse r = new MessageResponse();
        r.setId(m.getId());
        r.setSenderId(m.getSender().getId());
        r.setSenderName(m.getSender().getName());
        r.setReceiverId(m.getReceiver() != null ? m.getReceiver().getId() : null);
        r.setContent(m.getContent());
        r.setType(m.getType());
        r.setStatus(m.getStatus());
        r.setSentAt(m.getSentAt());
        return r;
    }
}
