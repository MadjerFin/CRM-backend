package com.wtc.controller;

import com.wtc.audit.Auditable;
import com.wtc.dto.request.SegmentRequest;
import com.wtc.dto.response.ApiResponse;
import com.wtc.entity.*;
import com.wtc.service.SegmentService;
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
@RequestMapping("/api/segments")
@RequiredArgsConstructor
@Tag(name = "Segmentos", description = "CRUD de segmentos de clientes")
@SecurityRequirement(name = "bearerAuth")
public class SegmentController {

    private final SegmentService segmentService;

    @GetMapping
    @Operation(summary = "Listar todos os segmentos")
    public ResponseEntity<ApiResponse<List<Segment>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(segmentService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar segmento por ID")
    public ResponseEntity<ApiResponse<Segment>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(segmentService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar segmento")
    @Auditable(action = "SEGMENT_CREATED", entity = "Segment")
    public ResponseEntity<ApiResponse<Segment>> create(
            @Valid @RequestBody SegmentRequest req,
            @AuthenticationPrincipal WtcUser operator) {
        return ResponseEntity.ok(ApiResponse.ok(segmentService.create(req, operator)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar segmento")
    @Auditable(action = "SEGMENT_UPDATED", entity = "Segment")
    public ResponseEntity<ApiResponse<Segment>> update(
            @PathVariable Long id, @Valid @RequestBody SegmentRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(segmentService.update(id, req)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir segmento")
    @Auditable(action = "SEGMENT_DELETED", entity = "Segment")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        segmentService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Segmento excluído", null));
    }
}
