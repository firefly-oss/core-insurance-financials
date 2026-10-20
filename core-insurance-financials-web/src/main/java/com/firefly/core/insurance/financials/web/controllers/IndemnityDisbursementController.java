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
import com.firefly.core.insurance.financials.core.services.IndemnityDisbursementService;
import com.firefly.core.insurance.financials.interfaces.dtos.IndemnityDisbursementDTO;
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
 * REST API for indemnity record pointing at a claim settlement and, once paid, a payment order.
 */
@RestController
@RequestMapping("/api/v1/indemnities")
@RequiredArgsConstructor
@Validated
@Tag(name = "Indemnity Disbursements", description = "Indemnity record pointing at a claim settlement and, once paid, a payment order")
public class IndemnityDisbursementController {

    private final IndemnityDisbursementService indemnityDisbursementService;

    @PostMapping("/filter")
    @Operation(summary = "Filter indemnityDisbursements", description = "Retrieve a page of indemnityDisbursements matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<IndemnityDisbursementDTO>>> filterIndemnityDisbursements(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<IndemnityDisbursementDTO> filterRequest) {
        return indemnityDisbursementService.filterIndemnityDisbursements(filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create IndemnityDisbursement", description = "Create a IndemnityDisbursement")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "IndemnityDisbursement created", content = @Content(schema = @Schema(implementation = IndemnityDisbursementDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<IndemnityDisbursementDTO>> createIndemnityDisbursement(
            @Parameter(description = "IndemnityDisbursement to create", required = true)
            @Valid @RequestBody IndemnityDisbursementDTO dto) {
        return indemnityDisbursementService.createIndemnityDisbursement(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{indemnityDisbursementId}")
    @Operation(summary = "Get IndemnityDisbursement by id", description = "Retrieve one IndemnityDisbursement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "IndemnityDisbursement found", content = @Content(schema = @Schema(implementation = IndemnityDisbursementDTO.class))),
        @ApiResponse(responseCode = "404", description = "IndemnityDisbursement not found", content = @Content)
    })
    public Mono<ResponseEntity<IndemnityDisbursementDTO>> getIndemnityDisbursementById(
            @Parameter(description = "Unique identifier of the IndemnityDisbursement", required = true)
            @PathVariable UUID indemnityDisbursementId) {
        return indemnityDisbursementService.getIndemnityDisbursementById(indemnityDisbursementId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{indemnityDisbursementId}")
    @Operation(summary = "Update IndemnityDisbursement", description = "Update an existing IndemnityDisbursement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "IndemnityDisbursement updated", content = @Content(schema = @Schema(implementation = IndemnityDisbursementDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "IndemnityDisbursement not found", content = @Content)
    })
    public Mono<ResponseEntity<IndemnityDisbursementDTO>> updateIndemnityDisbursement(
            @Parameter(description = "Unique identifier of the IndemnityDisbursement", required = true)
            @PathVariable UUID indemnityDisbursementId,
            @Parameter(description = "Updated IndemnityDisbursement", required = true)
            @Valid @RequestBody IndemnityDisbursementDTO dto) {
        return indemnityDisbursementService.updateIndemnityDisbursement(indemnityDisbursementId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{indemnityDisbursementId}")
    @Operation(summary = "Delete IndemnityDisbursement", description = "Delete a IndemnityDisbursement")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "IndemnityDisbursement deleted"),
        @ApiResponse(responseCode = "404", description = "IndemnityDisbursement not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteIndemnityDisbursement(
            @Parameter(description = "Unique identifier of the IndemnityDisbursement", required = true)
            @PathVariable UUID indemnityDisbursementId) {
        return indemnityDisbursementService.deleteIndemnityDisbursement(indemnityDisbursementId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
