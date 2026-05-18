package com.wtc.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {

    @Id
    private String id;

    private String userId;
    private String action;
    private String entity;
    private String entityId;
    private String details;
    private String ipAddress;

    @CreatedDate
    private LocalDateTime createdAt;
}
