package com.wtc.dto.response;

import com.wtc.enums.ClientStatus;
import com.wtc.enums.CompanyLevel;
import com.wtc.enums.Sector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClientResponse {
    private String id;
    private String userId;
    private String name;
    private String email;
    private String segmentId;
    private String segmentName;
    private String tags;
    private BigDecimal score;
    private ClientStatus status;
    private String phone;
    private String company;
    private Integer employeeCount;
    private Sector sector;
    private CompanyLevel companyLevel;
    private String website;
    private String city;
    private String state;
    private LocalDateTime createdAt;
}
