package com.wtc.dto.request;

import com.wtc.enums.ClientStatus;
import com.wtc.enums.CompanyLevel;
import com.wtc.enums.Sector;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ClientRequest {
    public Long userId;
    public Long segmentId;
    public String tags;
    public BigDecimal score;
    public ClientStatus status;
    public String phone;
    public String company;
    public Integer employeeCount;
    public Sector sector;
    public CompanyLevel companyLevel;
    public String website;
    public String city;
    public String state;
}
