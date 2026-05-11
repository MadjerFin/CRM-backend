package com.wtc.controller;

import com.wtc.audit.Auditable;
import com.wtc.dto.request.*;
import com.wtc.dto.response.*;
import com.wtc.entity.WtcUser;
import com.wtc.enums.ClientStatus;
import com.wtc.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/crm/clients")
@RequiredArgsConstructor
@Tag(name = "CRM - Clientes", description = "Gestão de clientes corporativos: CRUD, perfil 360°, anotações e busca")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @Operation(summary = "Listar clientes com filtros (status, segmento, tag)")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> findAll(
            @RequestParam(required = false) ClientStatus status,
            @RequestParam(required = false) Long segmentId,
            @RequestParam(required = false) String tag) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.findWithFilters(status, segmentId, tag)));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar clientes por nome, email ou empresa")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.search(q)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ApiResponse<ClientResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.findByIdAsResponse(id)));
    }

    @GetMapping("/{id}/profile360")
    @Operation(summary = "Perfil 360° — dados, mensagens recentes, campanhas, tarefas e anotações")
    public ResponseEntity<ApiResponse<Profile360Response>> profile360(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.getProfile360(id)));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo cliente CRM")
    @Auditable(action = "CLIENT_CREATED", entity = "Client")
    public ResponseEntity<ApiResponse<ClientResponse>> create(@RequestBody ClientRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok(clientService.create(req)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados CRM do cliente")
    @Auditable(action = "CLIENT_UPDATED", entity = "Client")
    public ResponseEntity<ApiResponse<ClientResponse>> update(
            @PathVariable Long id, @RequestBody ClientRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(clientService.update(id, req)));
    }

    @PostMapping("/{id}/annotations")
    @Operation(summary = "Adicionar anotação ao cliente")
    @Auditable(action = "ANNOTATION_ADDED", entity = "Client")
    public ResponseEntity<ApiResponse<Void>> addAnnotation(
            @PathVariable Long id,
            @RequestBody AnnotationRequest req,
            @AuthenticationPrincipal WtcUser operator) {
        clientService.addAnnotation(id, req, operator);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
