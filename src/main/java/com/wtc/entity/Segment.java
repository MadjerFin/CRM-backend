package com.wtc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WTC_SEGMENTS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_segments_gen")
    @SequenceGenerator(name = "seq_segments_gen", sequenceName = "SEQ_SEGMENTS", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    private WtcUser createdBy;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
