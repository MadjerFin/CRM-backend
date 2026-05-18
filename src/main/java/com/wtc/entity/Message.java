package com.wtc.entity;

import com.wtc.enums.MessageStatus;
import com.wtc.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {

    @Id
    private String id;

    private String senderId;
    private String senderName;
    private String receiverId;
    private String segmentId;
    private String content;

    @Builder.Default
    private MessageType type = MessageType.TEXT;

    @Builder.Default
    private MessageStatus status = MessageStatus.SENT;

    private String deeplink;

    @CreatedDate
    private LocalDateTime sentAt;

    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
}
