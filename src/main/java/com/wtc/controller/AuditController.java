package com.wtc.controller;

import com.wtc.dto.response.ApiResponse;
import com.wtc.entity.AuditLog;
import com.wtc.repository.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Auditoria", description = "Logs de operações (apenas operadores)")
@SecurityRequirement(name = "bearerAuth")
public class AuditController {

    private final AuditLogRepository auditLogRepo;

    @GetMapping
    @Operation(summary = "Listar todos os logs de auditoria")
    public ResponseEntity<ApiResponse<List<AuditLog>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(auditLogRepo.findAll()));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Logs de auditoria por usuário")
    public ResponseEntity<ApiResponse<List<AuditLog>>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(auditLogRepo.findByUserIdOrderByCreatedAtDesc(userId)));
    }

    @GetMapping("/entity/{entity}/{entityId}")
    @Operation(summary = "Logs de auditoria por entidade")
    public ResponseEntity<ApiResponse<List<AuditLog>>> findByEntity(
            @PathVariable String entity, @PathVariable Long entityId) {
        return ResponseEntity.ok(ApiResponse.ok(
            auditLogRepo.findByEntityAndEntityIdOrderByCreatedAtDesc(entity, entityId)));
    }
}
