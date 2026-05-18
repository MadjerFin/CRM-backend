package com.wtc.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "annotations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Annotation {

    @Id
    private String id;

    private String clientId;
    private String operatorId;
    private String operatorName;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;
}
