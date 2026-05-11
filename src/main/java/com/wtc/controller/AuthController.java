package com.wtc.controller;

import com.wtc.audit.Auditable;
import com.wtc.dto.request.*;
import com.wtc.dto.response.*;
import com.wtc.entity.WtcUser;
import com.wtc.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login, registro e FCM token")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login de operador ou cliente")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(req)));
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastro de novo usuário")
    @Auditable(action = "USER_REGISTERED", entity = "User")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(authService.register(req)));
    }

    @PutMapping("/fcm-token")
    @Operation(summary = "Atualizar token FCM do dispositivo")
    public ResponseEntity<ApiResponse<Void>> updateFcmToken(
            @AuthenticationPrincipal WtcUser user,
            @Valid @RequestBody UpdateFcmTokenRequest req) {
        authService.updateFcmToken(user.getId(), req.getFcmToken());
        return ResponseEntity.ok(ApiResponse.ok("Token atualizado", null));
    }
}
