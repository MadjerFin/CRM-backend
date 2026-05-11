package com.wtc.controller;

import com.wtc.audit.Auditable;
import com.wtc.dto.request.MessageRequest;
import com.wtc.dto.response.*;
import com.wtc.entity.WtcUser;
import com.wtc.enums.MessageStatus;
import com.wtc.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Chat & Mensageria", description = "Envio 1:1, por segmento e status de mensagens")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "Enviar mensagem (1:1 ou para segmento) — dispara push via Firebase")
    @Auditable(action = "MESSAGE_SENT", entity = "Message")
    public ResponseEntity<ApiResponse<MessageResponse>> send(
            @Valid @RequestBody MessageRequest req,
            @AuthenticationPrincipal WtcUser sender) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.send(req, sender)));
    }

    @GetMapping("/conversation/{userId}")
    @Operation(summary = "Histórico de conversa com um usuário")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.getConversation(userId)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da mensagem (enviado/entregue/lido/falha)")
    public ResponseEntity<ApiResponse<MessageResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam MessageStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.updateStatus(id, status)));
    }
}
