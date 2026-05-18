package com.wtc.entity;

import com.wtc.enums.ClientStatus;
import com.wtc.enums.CompanyLevel;
import com.wtc.enums.Sector;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "clients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private String userName;
    private String userEmail;
    private String segmentId;
    private String segmentName;
    private String tags;

    @Builder.Default
    private BigDecimal score = BigDecimal.ZERO;

    @Builder.Default
    private ClientStatus status = ClientStatus.ACTIVE;

    private String phone;
    private String company;
    private Integer employeeCount;
    private Sector sector;
    private CompanyLevel companyLevel;
    private String website;
    private String city;
    private String state;

    @CreatedDate  private LocalDateTime createdAt;
    @LastModifiedDate private LocalDateTime updatedAt;
}
