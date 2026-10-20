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
import io.fireflysoftware.core.insurance.financials.core.services.BordereauLineService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.BordereauLineDTO;
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
 * REST API for one premium, commission, claim, or refund line on a bordereau.
 */
@RestController
@RequestMapping("/api/v1/bordereaux/{bordereauId}/lines")
@RequiredArgsConstructor
@Validated
@Tag(name = "Bordereau Lines", description = "One premium, commission, claim, or refund line on a bordereau")
public class BordereauLineController {

    private final BordereauLineService bordereauLineService;

    @PostMapping("/filter")
    @Operation(summary = "Filter bordereauLines", description = "Retrieve a page of bordereauLines matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<BordereauLineDTO>>> filterBordereauLines(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<BordereauLineDTO> filterRequest) {
        return bordereauLineService.filterBordereauLines(bordereauId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create BordereauLine", description = "Create a BordereauLine")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "BordereauLine created", content = @Content(schema = @Schema(implementation = BordereauLineDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<BordereauLineDTO>> createBordereauLine(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId,
            @Parameter(description = "BordereauLine to create", required = true)
            @Valid @RequestBody BordereauLineDTO dto) {
        return bordereauLineService.createBordereauLine(bordereauId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{bordereauLineId}")
    @Operation(summary = "Get BordereauLine by id", description = "Retrieve one BordereauLine")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "BordereauLine found", content = @Content(schema = @Schema(implementation = BordereauLineDTO.class))),
        @ApiResponse(responseCode = "404", description = "BordereauLine not found", content = @Content)
    })
    public Mono<ResponseEntity<BordereauLineDTO>> getBordereauLineById(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId,
            @Parameter(description = "Unique identifier of the BordereauLine", required = true)
            @PathVariable UUID bordereauLineId) {
        return bordereauLineService.getBordereauLineById(bordereauId, bordereauLineId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{bordereauLineId}")
    @Operation(summary = "Update BordereauLine", description = "Update an existing BordereauLine")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "BordereauLine updated", content = @Content(schema = @Schema(implementation = BordereauLineDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "BordereauLine not found", content = @Content)
    })
    public Mono<ResponseEntity<BordereauLineDTO>> updateBordereauLine(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId,
            @Parameter(description = "Unique identifier of the BordereauLine", required = true)
            @PathVariable UUID bordereauLineId,
            @Parameter(description = "Updated BordereauLine", required = true)
            @Valid @RequestBody BordereauLineDTO dto) {
        return bordereauLineService.updateBordereauLine(bordereauId, bordereauLineId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{bordereauLineId}")
    @Operation(summary = "Delete BordereauLine", description = "Delete a BordereauLine")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "BordereauLine deleted"),
        @ApiResponse(responseCode = "404", description = "BordereauLine not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteBordereauLine(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId,
            @Parameter(description = "Unique identifier of the BordereauLine", required = true)
            @PathVariable UUID bordereauLineId) {
        return bordereauLineService.deleteBordereauLine(bordereauId, bordereauLineId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
