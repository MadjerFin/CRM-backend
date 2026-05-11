package com.wtc.controller;

import com.wtc.audit.Auditable;
import com.wtc.dto.request.CampaignRequest;
import com.wtc.dto.response.*;
import com.wtc.entity.WtcUser;
import com.wtc.service.CampaignService;
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
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
@Tag(name = "Campanhas Express", description = "Criação e envio imediato de campanhas por segmento")
@SecurityRequirement(name = "bearerAuth")
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    @Operation(summary = "Listar todas as campanhas")
    public ResponseEntity<ApiResponse<List<CampaignResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(campaignService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID")
    public ResponseEntity<ApiResponse<CampaignResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(campaignService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar campanha (rascunho)")
    @Auditable(action = "CAMPAIGN_CREATED", entity = "Campaign")
    public ResponseEntity<ApiResponse<CampaignResponse>> create(
            @Valid @RequestBody CampaignRequest req,
            @AuthenticationPrincipal WtcUser operator) {
        return ResponseEntity.ok(ApiResponse.ok(campaignService.create(req, operator)));
    }

    @PostMapping("/{id}/send")
    @Operation(summary = "Enviar campanha imediatamente para o segmento via Firebase Push")
    @Auditable(action = "CAMPAIGN_SENT", entity = "Campaign")
    public ResponseEntity<ApiResponse<CampaignResponse>> sendNow(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(campaignService.sendNow(id)));
    }
}
