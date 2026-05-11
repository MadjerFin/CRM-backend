package com.wtc.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SegmentRequest {
    @NotBlank public String name;
    public String description;
}
