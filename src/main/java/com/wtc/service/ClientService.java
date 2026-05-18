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

        String segName = null;
        if (req.getSegmentId() != null) {
            segName = segmentRepo.findById(req.getSegmentId())
                .map(Segment::getName).orElse(null);
        }

        Client c = Client.builder()
            .userId(user.getId())
            .userName(user.getName())
            .userEmail(user.getEmail())
            .segmentId(req.getSegmentId())
            .segmentName(segName)
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
        return toResponse(clientRepo.save(c));
    }

    public ClientResponse update(String id, ClientRequest req) {
        Client c = clientRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (req.getSegmentId() != null) {
            c.setSegmentId(req.getSegmentId());
            c.setSegmentName(segmentRepo.findById(req.getSegmentId()).map(Segment::getName).orElse(null));
        }
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
        return toResponse(clientRepo.save(c));
    }

    public List<ClientResponse> findWithFilters(ClientStatus status, String segmentId, String tag) {
        return clientRepo.findAll().stream()
            .filter(c -> status == null || c.getStatus() == status)
            .filter(c -> segmentId == null || segmentId.equals(c.getSegmentId()))
            .filter(c -> tag == null || (c.getTags() != null && c.getTags().toLowerCase().contains(tag.toLowerCase())))
            .map(this::toResponse).toList();
    }

    public List<ClientResponse> search(String q) {
        return clientRepo.search(q).stream().map(this::toResponse).toList();
    }

    public ClientResponse findByIdAsResponse(String id) {
        return toResponse(clientRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado")));
    }

    public Client findById(String id) {
        return clientRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public void delete(String id) { clientRepo.deleteById(id); }

    public void addAnnotation(String clientId, AnnotationRequest req, WtcUser operator) {
        Annotation a = Annotation.builder()
            .clientId(clientId)
            .operatorId(operator.getId())
            .operatorName(operator.getName())
            .content(req.getContent())
            .build();
        annotationRepo.save(a);
    }

    public Profile360Response getProfile360(String clientId) {
        Client client = findById(clientId);

        Profile360Response p = new Profile360Response();
        p.setClient(toResponse(client));

        p.setLastMessages(messageRepo.findConversation(client.getUserId()).stream()
            .limit(10).map(this::toMessageResponse).toList());

        p.setLastCampaigns(campRecipRepo.findByClientId(clientId).stream()
            .limit(5).map(r -> {
                var cr = new CampaignResponse();
                cr.setId(r.getCampaignId());
                cr.setStatus(r.getStatus() != null ? com.wtc.enums.CampaignStatus.SENT : null);
                return cr;
            }).toList());

        p.setOpenTasks(taskRepo.findByAssignedToIdAndStatus(client.getUserId(), TaskStatus.OPEN)
            .stream().map(Task::getTitle).toList());

        p.setAnnotations(annotationRepo.findByClientIdOrderByCreatedAtDesc(clientId)
            .stream().limit(5)
            .map(a -> a.getOperatorName() + ": " + a.getContent()).toList());

        return p;
    }

    public ClientResponse toResponse(Client c) {
        return ClientResponse.builder()
            .id(c.getId())
            .userId(c.getUserId())
            .name(c.getUserName())
            .email(c.getUserEmail())
            .segmentId(c.getSegmentId())
            .segmentName(c.getSegmentName())
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
        return MessageResponse.builder()
            .id(m.getId())
            .senderId(m.getSenderId())
            .senderName(m.getSenderName())
            .receiverId(m.getReceiverId())
            .content(m.getContent())
            .type(m.getType())
            .status(m.getStatus())
            .sentAt(m.getSentAt())
            .build();
    }
}
