package com.wtc.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnnotationRequest {
    @NotBlank public String content;
}
