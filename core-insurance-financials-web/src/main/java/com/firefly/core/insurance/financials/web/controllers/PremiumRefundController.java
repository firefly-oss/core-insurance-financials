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


package com.firefly.core.insurance.financials.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.financials.core.services.PremiumRefundService;
import com.firefly.core.insurance.financials.interfaces.dtos.PremiumRefundDTO;
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
 * REST API for refund recorded against a premium schedule.
 */
@RestController
@RequestMapping("/api/v1/premium-schedules/{premiumScheduleId}/refunds")
@RequiredArgsConstructor
@Validated
@Tag(name = "Premium Refunds", description = "Refund recorded against a premium schedule")
public class PremiumRefundController {

    private final PremiumRefundService premiumRefundService;

    @PostMapping("/filter")
    @Operation(summary = "Filter premiumRefunds", description = "Retrieve a page of premiumRefunds matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<PremiumRefundDTO>>> filterPremiumRefunds(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<PremiumRefundDTO> filterRequest) {
        return premiumRefundService.filterPremiumRefunds(premiumScheduleId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create PremiumRefund", description = "Create a PremiumRefund")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "PremiumRefund created", content = @Content(schema = @Schema(implementation = PremiumRefundDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<PremiumRefundDTO>> createPremiumRefund(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "PremiumRefund to create", required = true)
            @Valid @RequestBody PremiumRefundDTO dto) {
        return premiumRefundService.createPremiumRefund(premiumScheduleId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{premiumRefundId}")
    @Operation(summary = "Get PremiumRefund by id", description = "Retrieve one PremiumRefund")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumRefund found", content = @Content(schema = @Schema(implementation = PremiumRefundDTO.class))),
        @ApiResponse(responseCode = "404", description = "PremiumRefund not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumRefundDTO>> getPremiumRefundById(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumRefund", required = true)
            @PathVariable UUID premiumRefundId) {
        return premiumRefundService.getPremiumRefundById(premiumScheduleId, premiumRefundId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{premiumRefundId}")
    @Operation(summary = "Update PremiumRefund", description = "Update an existing PremiumRefund")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumRefund updated", content = @Content(schema = @Schema(implementation = PremiumRefundDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "PremiumRefund not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumRefundDTO>> updatePremiumRefund(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumRefund", required = true)
            @PathVariable UUID premiumRefundId,
            @Parameter(description = "Updated PremiumRefund", required = true)
            @Valid @RequestBody PremiumRefundDTO dto) {
        return premiumRefundService.updatePremiumRefund(premiumScheduleId, premiumRefundId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{premiumRefundId}")
    @Operation(summary = "Delete PremiumRefund", description = "Delete a PremiumRefund")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "PremiumRefund deleted"),
        @ApiResponse(responseCode = "404", description = "PremiumRefund not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deletePremiumRefund(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumRefund", required = true)
            @PathVariable UUID premiumRefundId) {
        return premiumRefundService.deletePremiumRefund(premiumScheduleId, premiumRefundId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
