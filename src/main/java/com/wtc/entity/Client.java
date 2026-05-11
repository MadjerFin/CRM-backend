package com.wtc.entity;

import com.wtc.enums.ClientStatus;
import com.wtc.enums.CompanyLevel;
import com.wtc.enums.Sector;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "WTC_CLIENTS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clients_gen")
    @SequenceGenerator(name = "seq_clients_gen", sequenceName = "SEQ_CLIENTS", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private WtcUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID")
    private Segment segment;

    @Column(length = 500)
    private String tags;

    @Column(precision = 5, scale = 2)
    private BigDecimal score = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ClientStatus status = ClientStatus.ACTIVE;

    @Column(length = 30)
    private String phone;

    @Column(length = 200)
    private String company;

    @Column(name = "EMPLOYEE_COUNT")
    private Integer employeeCount;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Sector sector;

    @Enumerated(EnumType.STRING)
    @Column(name = "COMPANY_LEVEL", length = 20)
    private CompanyLevel companyLevel;

    @Column(length = 500)
    private String website;

    @Column(length = 100)
    private String city;

    @Column(length = 2)
    private String state;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
