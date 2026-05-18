package com.wtc.entity;

import com.wtc.enums.RecipientStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "campaign_recipients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampaignRecipient {

    @Id
    private String id;

    private String campaignId;
    private String clientId;

    @Builder.Default
    private RecipientStatus status = RecipientStatus.PENDING;

    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
}
