package com.wtc.dto.request;
import com.wtc.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank public String name;
    @Email @NotBlank public String email;
    @NotBlank @Size(min = 6) public String password;
    @NotNull public UserRole role;
}
