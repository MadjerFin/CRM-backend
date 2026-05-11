package com.wtc.dto.request;
import com.wtc.enums.CampaignType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CampaignRequest {
    @NotBlank public String title;
    @NotBlank public String body;
    public CampaignType type = CampaignType.PROMO;
    @NotNull public Long segmentId;
    public String deeplink;
}
