package com.wtc.entity;

import com.wtc.enums.CampaignStatus;
import com.wtc.enums.CampaignType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WTC_CAMPAIGNS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_campaigns_gen")
    @SequenceGenerator(name = "seq_campaigns_gen", sequenceName = "SEQ_CAMPAIGNS", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 4000)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CampaignType type = CampaignType.PROMO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID", nullable = false)
    private Segment segment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    private WtcUser createdBy;

    @Column(length = 500)
    private String deeplink;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CampaignStatus status = CampaignStatus.DRAFT;

    @Column(name = "SENT_AT")
    private LocalDateTime sentAt;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
