package com.wtc.dto.response;

import com.wtc.enums.CampaignStatus;
import com.wtc.enums.CampaignType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CampaignResponse {
    private String id;
    private String title;
    private String body;
    private CampaignType type;
    private String segmentName;
    private CampaignStatus status;
    private String deeplink;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
