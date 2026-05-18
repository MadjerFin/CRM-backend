package com.wtc.entity;

import com.wtc.enums.CampaignStatus;
import com.wtc.enums.CampaignType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "campaigns")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Campaign {

    @Id
    private String id;

    private String title;
    private String body;

    @Builder.Default
    private CampaignType type = CampaignType.PROMO;

    private String segmentId;
    private String segmentName;
    private String createdById;
    private String deeplink;

    @Builder.Default
    private CampaignStatus status = CampaignStatus.DRAFT;

    private LocalDateTime sentAt;

    @CreatedDate
    private LocalDateTime createdAt;
}
