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
import com.firefly.core.insurance.financials.core.services.CommissionEntryService;
import com.firefly.core.insurance.financials.interfaces.dtos.CommissionEntryDTO;
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
 * REST API for commission accrued for a party or distributor against a premium schedule.
 */
@RestController
@RequestMapping("/api/v1/premium-schedules/{premiumScheduleId}/commissions")
@RequiredArgsConstructor
@Validated
@Tag(name = "Commission Entries", description = "Commission accrued for a party or distributor against a premium schedule")
public class CommissionEntryController {

    private final CommissionEntryService commissionEntryService;

    @PostMapping("/filter")
    @Operation(summary = "Filter commissionEntries", description = "Retrieve a page of commissionEntries matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<CommissionEntryDTO>>> filterCommissionEntries(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<CommissionEntryDTO> filterRequest) {
        return commissionEntryService.filterCommissionEntries(premiumScheduleId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create CommissionEntry", description = "Create a CommissionEntry")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CommissionEntry created", content = @Content(schema = @Schema(implementation = CommissionEntryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<CommissionEntryDTO>> createCommissionEntry(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "CommissionEntry to create", required = true)
            @Valid @RequestBody CommissionEntryDTO dto) {
        return commissionEntryService.createCommissionEntry(premiumScheduleId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{commissionEntryId}")
    @Operation(summary = "Get CommissionEntry by id", description = "Retrieve one CommissionEntry")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CommissionEntry found", content = @Content(schema = @Schema(implementation = CommissionEntryDTO.class))),
        @ApiResponse(responseCode = "404", description = "CommissionEntry not found", content = @Content)
    })
    public Mono<ResponseEntity<CommissionEntryDTO>> getCommissionEntryById(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the CommissionEntry", required = true)
            @PathVariable UUID commissionEntryId) {
        return commissionEntryService.getCommissionEntryById(premiumScheduleId, commissionEntryId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{commissionEntryId}")
    @Operation(summary = "Update CommissionEntry", description = "Update an existing CommissionEntry")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CommissionEntry updated", content = @Content(schema = @Schema(implementation = CommissionEntryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "CommissionEntry not found", content = @Content)
    })
    public Mono<ResponseEntity<CommissionEntryDTO>> updateCommissionEntry(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the CommissionEntry", required = true)
            @PathVariable UUID commissionEntryId,
            @Parameter(description = "Updated CommissionEntry", required = true)
            @Valid @RequestBody CommissionEntryDTO dto) {
        return commissionEntryService.updateCommissionEntry(premiumScheduleId, commissionEntryId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{commissionEntryId}")
    @Operation(summary = "Delete CommissionEntry", description = "Delete a CommissionEntry")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "CommissionEntry deleted"),
        @ApiResponse(responseCode = "404", description = "CommissionEntry not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteCommissionEntry(
            @Parameter(description = "Unique identifier of the PremiumSchedule", required = true)
            @PathVariable UUID premiumScheduleId,
            @Parameter(description = "Unique identifier of the CommissionEntry", required = true)
            @PathVariable UUID commissionEntryId) {
        return commissionEntryService.deleteCommissionEntry(premiumScheduleId, commissionEntryId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
