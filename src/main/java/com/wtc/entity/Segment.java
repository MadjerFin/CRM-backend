package com.wtc.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "segments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Segment {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;
    private String createdById;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
