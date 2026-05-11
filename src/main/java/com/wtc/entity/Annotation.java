package com.wtc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WTC_ANNOTATIONS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_annotations_gen")
    @SequenceGenerator(name = "seq_annotations_gen", sequenceName = "SEQ_ANNOTATIONS", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPERATOR_ID", nullable = false)
    private WtcUser operator;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
