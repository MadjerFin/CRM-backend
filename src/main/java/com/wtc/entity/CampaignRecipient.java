package com.wtc.entity;

import com.wtc.enums.RecipientStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WTC_CAMPAIGN_RECIPIENTS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampaignRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_camp_recip_gen")
    @SequenceGenerator(name = "seq_camp_recip_gen", sequenceName = "SEQ_CAMP_RECIP", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAMPAIGN_ID", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RecipientStatus status = RecipientStatus.PENDING;

    @Column(name = "SENT_AT")      private LocalDateTime sentAt;
    @Column(name = "DELIVERED_AT") private LocalDateTime deliveredAt;
    @Column(name = "READ_AT")      private LocalDateTime readAt;
}
