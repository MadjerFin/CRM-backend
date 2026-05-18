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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Chat & Mensageria")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "Enviar mensagem (1:1 ou por segmento)")
    @Auditable(action = "MESSAGE_SENT", entity = "Message")
    public ResponseEntity<ApiResponse<MessageResponse>> send(
            @RequestBody MessageRequest req,
            @AuthenticationPrincipal WtcUser sender) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.send(req, sender)));
    }

    @GetMapping("/conversation/{userId}")
    @Operation(summary = "Histórico de conversa entre dois usuários")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.getConversation(userId)));
    }

    @GetMapping("/inbox/{clientId}")
    @Operation(summary = "Inbox do cliente — mensagens recebidas ordenadas por data")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getInbox(@PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.getInbox(clientId)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da mensagem")
    public ResponseEntity<ApiResponse<MessageResponse>> updateStatus(
            @PathVariable String id, @RequestParam MessageStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.updateStatus(id, status)));
    }
}
