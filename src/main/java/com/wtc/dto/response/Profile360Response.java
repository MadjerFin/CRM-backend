package com.wtc.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class Profile360Response {
    private ClientResponse client;
    private List<MessageResponse> lastMessages;
    private List<CampaignResponse> lastCampaigns;
    private List<String> openTasks;
    private List<String> annotations;
}
