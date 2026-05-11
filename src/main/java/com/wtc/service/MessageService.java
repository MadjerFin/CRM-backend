package com.wtc.service;

import com.wtc.dto.request.MessageRequest;
import com.wtc.dto.response.MessageResponse;
import com.wtc.entity.*;
import com.wtc.enums.MessageStatus;
import com.wtc.firebase.FirebaseMessagingService;
import com.wtc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepo;
    private final WtcUserRepository userRepo;
    private final SegmentRepository segmentRepo;
    private final ClientRepository clientRepo;
    private final FirebaseMessagingService firebase;

    public MessageResponse send(MessageRequest req, WtcUser sender) {
        Message msg = Message.builder()
            .sender(sender)
            .content(req.getContent())
            .type(req.getType())
            .deeplink(req.getDeeplink())
            .status(MessageStatus.SENT)
            .build();

        if (req.getReceiverId() != null) {
            WtcUser receiver = userRepo.findById(req.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));
            msg.setReceiver(receiver);
            messageRepo.save(msg);
            // Push para o destinatário
            firebase.sendToToken(receiver.getFcmToken(),
                "Nova mensagem de " + sender.getName(),
                req.getContent(),
                Map.of("messageId", msg.getId().toString(),
                       "deeplink", req.getDeeplink() != null ? req.getDeeplink() : ""));
        } else if (req.getSegmentId() != null) {
            Segment segment = segmentRepo.findById(req.getSegmentId())
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
            msg.setSegment(segment);
            messageRepo.save(msg);
            // Push para todos do segmento
            List<String> tokens = clientRepo.findBySegmentId(req.getSegmentId()).stream()
                .map(c -> c.getUser().getFcmToken())
                .filter(t -> t != null && !t.isBlank())
                .toList();
            firebase.sendToMultipleTokens(tokens,
                "Nova mensagem de " + sender.getName(), req.getContent(),
                Map.of("segmentId", segment.getId().toString()));
        } else {
            throw new RuntimeException("Informe receiverId ou segmentId");
        }
        return toResponse(msg);
    }

    public MessageResponse updateStatus(Long id, MessageStatus status) {
        Message msg = messageRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
        msg.setStatus(status);
        if (status == MessageStatus.DELIVERED) msg.setDeliveredAt(LocalDateTime.now());
        if (status == MessageStatus.READ)      msg.setReadAt(LocalDateTime.now());
        return toResponse(messageRepo.save(msg));
    }

    public List<MessageResponse> getConversation(Long userId) {
        return messageRepo.findConversation(userId).stream().map(this::toResponse).toList();
    }

    private MessageResponse toResponse(Message m) {
        MessageResponse r = new MessageResponse();
        r.setId(m.getId());
        r.setSenderId(m.getSender().getId());
        r.setSenderName(m.getSender().getName());
        r.setReceiverId(m.getReceiver() != null ? m.getReceiver().getId() : null);
        r.setSegmentId(m.getSegment() != null ? m.getSegment().getId() : null);
        r.setContent(m.getContent());
        r.setType(m.getType());
        r.setStatus(m.getStatus());
        r.setDeeplink(m.getDeeplink());
        r.setSentAt(m.getSentAt());
        return r;
    }
}
