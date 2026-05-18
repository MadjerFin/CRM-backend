package com.wtc.dto.response;

import com.wtc.enums.MessageStatus;
import com.wtc.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageResponse {
    private String id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String segmentId;
    private String content;
    private MessageType type;
    private MessageStatus status;
    private String deeplink;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
}
