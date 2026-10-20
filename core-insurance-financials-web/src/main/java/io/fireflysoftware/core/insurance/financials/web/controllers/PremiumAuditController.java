/*
 * Copyright 2026 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.core.insurance.financials.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.financials.core.services.PremiumAuditService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.PremiumAuditDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST API for premium audit adjustment from declared exposure. The rating figure is recorded, not calculated here..
 */
@RestController
@RequestMapping("/api/v1/premium-schedules/{premiumScheduleId}/audits")
@RequiredArgsConstructor
@Validated
@Tag(name = "Premium Audits", description = "Premium audit adjustment from declared exposure. The rating figure is recorded, not calculated here.")
public class PremiumAuditController {

    private final PremiumAuditService premiumAuditService;

    @PostMapping("/filter")
    @Operation(summary = "Filter premiumAudits", description = "Retrieve a page of premiumAudits matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<PremiumAuditDTO>>> filterPremiumAudits(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<PremiumAuditDTO> filterRequest) {
        return premiumAuditService.filterPremiumAudits(premiumScheduleId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create PremiumAudit", description = "Create a PremiumAudit")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "PremiumAudit created", content = @Content(schema = @Schema(implementation = PremiumAuditDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<PremiumAuditDTO>> createPremiumAudit(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "PremiumAudit to create", required = true)
            @Valid @RequestBody PremiumAuditDTO dto) {
        return premiumAuditService.createPremiumAudit(premiumScheduleId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{premiumAuditId}")
    @Operation(summary = "Get PremiumAudit by id", description = "Retrieve one PremiumAudit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumAudit found", content = @Content(schema = @Schema(implementation = PremiumAuditDTO.class))),
        @ApiResponse(responseCode = "404", description = "PremiumAudit not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumAuditDTO>> getPremiumAuditById(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumAudit", required = true)
            @PathVariable UUID premiumAuditId) {
        return premiumAuditService.getPremiumAuditById(premiumScheduleId, premiumAuditId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{premiumAuditId}")
    @Operation(summary = "Update PremiumAudit", description = "Update an existing PremiumAudit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumAudit updated", content = @Content(schema = @Schema(implementation = PremiumAuditDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "PremiumAudit not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumAuditDTO>> updatePremiumAudit(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumAudit", required = true)
            @PathVariable UUID premiumAuditId,
            @Parameter(description = "Updated PremiumAudit", required = true)
            @Valid @RequestBody PremiumAuditDTO dto) {
        return premiumAuditService.updatePremiumAudit(premiumScheduleId, premiumAuditId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{premiumAuditId}")
    @Operation(summary = "Delete PremiumAudit", description = "Delete a PremiumAudit")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "PremiumAudit deleted"),
        @ApiResponse(responseCode = "404", description = "PremiumAudit not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deletePremiumAudit(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumAudit", required = true)
            @PathVariable UUID premiumAuditId) {
        return premiumAuditService.deletePremiumAudit(premiumScheduleId, premiumAuditId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
