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
import io.fireflysoftware.core.insurance.financials.core.services.CreditControlCaseService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.CreditControlCaseDTO;
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
 * REST API for arrears follow-up and lapse notice against a premium schedule.
 */
@RestController
@RequestMapping("/api/v1/premium-schedules/{premiumScheduleId}/credit-control")
@RequiredArgsConstructor
@Validated
@Tag(name = "Credit Control", description = "Arrears follow-up and lapse notice against a premium schedule")
public class CreditControlCaseController {

    private final CreditControlCaseService creditControlCaseService;

    @PostMapping("/filter")
    @Operation(summary = "Filter creditControlCases", description = "Retrieve a page of creditControlCases matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<CreditControlCaseDTO>>> filterCreditControlCases(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<CreditControlCaseDTO> filterRequest) {
        return creditControlCaseService.filterCreditControlCases(premiumScheduleId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create CreditControlCase", description = "Create a CreditControlCase")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CreditControlCase created", content = @Content(schema = @Schema(implementation = CreditControlCaseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<CreditControlCaseDTO>> createCreditControlCase(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "CreditControlCase to create", required = true)
            @Valid @RequestBody CreditControlCaseDTO dto) {
        return creditControlCaseService.createCreditControlCase(premiumScheduleId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{creditControlCaseId}")
    @Operation(summary = "Get CreditControlCase by id", description = "Retrieve one CreditControlCase")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CreditControlCase found", content = @Content(schema = @Schema(implementation = CreditControlCaseDTO.class))),
        @ApiResponse(responseCode = "404", description = "CreditControlCase not found", content = @Content)
    })
    public Mono<ResponseEntity<CreditControlCaseDTO>> getCreditControlCaseById(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the CreditControlCase", required = true)
            @PathVariable UUID creditControlCaseId) {
        return creditControlCaseService.getCreditControlCaseById(premiumScheduleId, creditControlCaseId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{creditControlCaseId}")
    @Operation(summary = "Update CreditControlCase", description = "Update an existing CreditControlCase")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CreditControlCase updated", content = @Content(schema = @Schema(implementation = CreditControlCaseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "CreditControlCase not found", content = @Content)
    })
    public Mono<ResponseEntity<CreditControlCaseDTO>> updateCreditControlCase(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the CreditControlCase", required = true)
            @PathVariable UUID creditControlCaseId,
            @Parameter(description = "Updated CreditControlCase", required = true)
            @Valid @RequestBody CreditControlCaseDTO dto) {
        return creditControlCaseService.updateCreditControlCase(premiumScheduleId, creditControlCaseId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{creditControlCaseId}")
    @Operation(summary = "Delete CreditControlCase", description = "Delete a CreditControlCase")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "CreditControlCase deleted"),
        @ApiResponse(responseCode = "404", description = "CreditControlCase not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteCreditControlCase(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the CreditControlCase", required = true)
            @PathVariable UUID creditControlCaseId) {
        return creditControlCaseService.deleteCreditControlCase(premiumScheduleId, creditControlCaseId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
