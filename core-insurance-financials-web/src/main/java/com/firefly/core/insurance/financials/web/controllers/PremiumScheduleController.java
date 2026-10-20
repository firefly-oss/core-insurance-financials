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
import com.firefly.core.insurance.financials.core.services.PremiumScheduleService;
import com.firefly.core.insurance.financials.interfaces.dtos.PremiumScheduleDTO;
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
 * REST API for premium schedule for a policy.
 */
@RestController
@RequestMapping("/api/v1/premium-schedules")
@RequiredArgsConstructor
@Validated
@Tag(name = "Premium Schedules", description = "Premium schedule for a policy")
public class PremiumScheduleController {

    private final PremiumScheduleService premiumScheduleService;

    @PostMapping("/filter")
    @Operation(summary = "Filter premiumSchedules", description = "Retrieve a page of premiumSchedules matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<PremiumScheduleDTO>>> filterPremiumSchedules(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<PremiumScheduleDTO> filterRequest) {
        return premiumScheduleService.filterPremiumSchedules(filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create PremiumSchedule", description = "Create a PremiumSchedule")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "PremiumSchedule created", content = @Content(schema = @Schema(implementation = PremiumScheduleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<PremiumScheduleDTO>> createPremiumSchedule(
            @Parameter(description = "PremiumSchedule to create", required = true)
            @Valid @RequestBody PremiumScheduleDTO dto) {
        return premiumScheduleService.createPremiumSchedule(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{premiumScheduleId}")
    @Operation(summary = "Get PremiumSchedule by id", description = "Retrieve one PremiumSchedule")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumSchedule found", content = @Content(schema = @Schema(implementation = PremiumScheduleDTO.class))),
        @ApiResponse(responseCode = "404", description = "PremiumSchedule not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumScheduleDTO>> getPremiumScheduleById(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId) {
        return premiumScheduleService.getPremiumScheduleById(premiumScheduleId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{premiumScheduleId}")
    @Operation(summary = "Update PremiumSchedule", description = "Update an existing PremiumSchedule")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PremiumSchedule updated", content = @Content(schema = @Schema(implementation = PremiumScheduleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "PremiumSchedule not found", content = @Content)
    })
    public Mono<ResponseEntity<PremiumScheduleDTO>> updatePremiumSchedule(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Updated PremiumSchedule", required = true)
            @Valid @RequestBody PremiumScheduleDTO dto) {
        return premiumScheduleService.updatePremiumSchedule(premiumScheduleId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{premiumScheduleId}")
    @Operation(summary = "Delete PremiumSchedule", description = "Delete a PremiumSchedule")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "PremiumSchedule deleted"),
        @ApiResponse(responseCode = "404", description = "PremiumSchedule not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deletePremiumSchedule(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId) {
        return premiumScheduleService.deletePremiumSchedule(premiumScheduleId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
