package com.wtc.entity;

import com.wtc.enums.MessageStatus;
import com.wtc.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WTC_MESSAGES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_messages_gen")
    @SequenceGenerator(name = "seq_messages_gen", sequenceName = "SEQ_MESSAGES", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private WtcUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID")
    private WtcUser receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID")
    private Segment segment;

    @Column(nullable = false, length = 4000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MessageType type = MessageType.TEXT;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MessageStatus status = MessageStatus.SENT;

    @Column(length = 500)
    private String deeplink;

    @Column(name = "SENT_AT", nullable = false, updatable = false)
    private LocalDateTime sentAt;

    @Column(name = "DELIVERED_AT")
    private LocalDateTime deliveredAt;

    @Column(name = "READ_AT")
    private LocalDateTime readAt;

    @PrePersist protected void onCreate() { sentAt = LocalDateTime.now(); }
}
