package com.wtc.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateFcmTokenRequest {
    @NotBlank public String fcmToken;
}
