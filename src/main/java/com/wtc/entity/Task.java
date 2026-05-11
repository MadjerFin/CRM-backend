package com.wtc.entity;

import com.wtc.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WTC_TASKS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tasks_gen")
    @SequenceGenerator(name = "seq_tasks_gen", sequenceName = "SEQ_TASKS", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ID")
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_TO", nullable = false)
    private WtcUser assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    private WtcUser createdBy;

    @Column(nullable = false, length = 300)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TaskStatus status = TaskStatus.OPEN;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
