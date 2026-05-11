package com.wtc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank
    private String title;
    @NotNull
    private Long assignedToId;
    private Long messageId;
}
