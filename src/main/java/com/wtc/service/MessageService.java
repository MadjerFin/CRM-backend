package com.wtc.service;

import com.wtc.dto.request.MessageRequest;
import com.wtc.dto.response.MessageResponse;
import com.wtc.entity.*;
import com.wtc.enums.MessageStatus;
import com.wtc.enums.MessageType;
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
            .senderId(sender.getId())
            .senderName(sender.getName())
            .content(req.getContent())
            .type(req.getType() != null ? req.getType() : MessageType.TEXT)
            .status(MessageStatus.SENT)
            .deeplink(req.getDeeplink())
            .build();

        if (req.getReceiverId() != null) {
            WtcUser receiver = userRepo.findById(req.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));
            msg.setReceiverId(receiver.getId());
            messageRepo.save(msg);
            if (receiver.getFcmToken() != null) {
                firebase.sendToToken(receiver.getFcmToken(),
                    "Nova mensagem de " + sender.getName(), req.getContent(),
                    Map.of("type", "CHAT", "senderId", sender.getId(),
                           "deeplink", req.getDeeplink() != null ? req.getDeeplink() : ""));
            }
        } else if (req.getSegmentId() != null) {
            Segment seg = segmentRepo.findById(req.getSegmentId())
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
            msg.setSegmentId(seg.getId());
            messageRepo.save(msg);
            List<String> tokens = clientRepo.findBySegmentId(seg.getId())
                .stream().map(c -> userRepo.findById(c.getUserId())
                    .map(WtcUser::getFcmToken).orElse(null))
                .filter(t -> t != null && !t.isBlank()).toList();
            firebase.sendToMultipleTokens(tokens, "Nova mensagem para " + seg.getName(),
                req.getContent(), Map.of("type", "CHAT_SEGMENT", "segmentId", seg.getId()));
        } else {
            throw new RuntimeException("Informe receiverId ou segmentId");
        }
        return toResponse(msg);
    }

    public List<MessageResponse> getConversation(String userId) {
        return messageRepo.findConversation(userId).stream().map(this::toResponse).toList();
    }

    public List<MessageResponse> getInbox(String clientId) {
        return messageRepo.findByReceiverIdOrderBySentAtDesc(clientId)
            .stream().map(this::toResponse).toList();
    }

    public MessageResponse updateStatus(String messageId, MessageStatus status) {
        Message msg = messageRepo.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
        msg.setStatus(status);
        if (status == MessageStatus.DELIVERED) msg.setDeliveredAt(LocalDateTime.now());
        if (status == MessageStatus.READ) msg.setReadAt(LocalDateTime.now());
        return toResponse(messageRepo.save(msg));
    }

    private MessageResponse toResponse(Message m) {
        return MessageResponse.builder()
            .id(m.getId())
            .senderId(m.getSenderId())
            .senderName(m.getSenderName())
            .receiverId(m.getReceiverId())
            .segmentId(m.getSegmentId())
            .content(m.getContent())
            .type(m.getType())
            .status(m.getStatus())
            .deeplink(m.getDeeplink())
            .sentAt(m.getSentAt())
            .deliveredAt(m.getDeliveredAt())
            .readAt(m.getReadAt())
            .build();
    }
}
