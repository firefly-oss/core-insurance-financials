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
import io.fireflysoftware.core.insurance.financials.core.services.PremiumInstalmentService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.PremiumInstalmentDTO;
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
 * REST API for one instalment on a premium schedule.
 */
@RestController
@RequestMapping("/api/v1/premium-schedules/{premiumScheduleId}/instalments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Premium Instalments", description = "One instalment on a premium schedule")
public class PremiumInstalmentController {

    private final PremiumInstalmentService premiumInstalmentService;

    @PostMapping("/filter")
    @Operation(summary = "Filter premiumInstalments", description = "Retrieve a page of premiumInstalments matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<PremiumInstalmentDTO>>> filterPremiumInstalments(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<PremiumInstalmentDTO> filterRequest) {
        return premiumInstalmentService.filterPremiumInstalments(premiumScheduleId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create PremiumInstalment", description = "Create a PremiumInstalment")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "PremiumInstalment created", content = @Content(schema = @Schema(implementation = PremiumInstalmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<PremiumInstalmentDTO>> createPremiumInstalment(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "PremiumInstalment to create", required = true)
            @Valid @RequestBody PremiumInstalmentDTO dto) {
        return premiumInstalmentService.createPremiumInstalment(premiumScheduleId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{premiumInstalmentId}")
    @Operation(summary = "Get PremiumInstalment by id", description = "Retrieve one PremiumInstalment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumInstalment found", content = @Content(schema = @Schema(implementation = PremiumInstalmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "PremiumInstalment not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumInstalmentDTO>> getPremiumInstalmentById(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumInstalment", required = true)
            @PathVariable UUID premiumInstalmentId) {
        return premiumInstalmentService.getPremiumInstalmentById(premiumScheduleId, premiumInstalmentId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{premiumInstalmentId}")
    @Operation(summary = "Update PremiumInstalment", description = "Update an existing PremiumInstalment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumInstalment updated", content = @Content(schema = @Schema(implementation = PremiumInstalmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "PremiumInstalment not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumInstalmentDTO>> updatePremiumInstalment(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumInstalment", required = true)
            @PathVariable UUID premiumInstalmentId,
            @Parameter(description = "Updated PremiumInstalment", required = true)
            @Valid @RequestBody PremiumInstalmentDTO dto) {
        return premiumInstalmentService.updatePremiumInstalment(premiumScheduleId, premiumInstalmentId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{premiumInstalmentId}")
    @Operation(summary = "Delete PremiumInstalment", description = "Delete a PremiumInstalment")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "PremiumInstalment deleted"),
        @ApiResponse(responseCode = "404", description = "PremiumInstalment not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deletePremiumInstalment(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the PremiumInstalment", required = true)
            @PathVariable UUID premiumInstalmentId) {
        return premiumInstalmentService.deletePremiumInstalment(premiumScheduleId, premiumInstalmentId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
