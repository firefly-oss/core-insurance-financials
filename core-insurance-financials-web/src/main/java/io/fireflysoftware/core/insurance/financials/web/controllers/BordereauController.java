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
import io.fireflysoftware.core.insurance.financials.core.services.BordereauService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.BordereauDTO;
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
 * REST API for bordereau for a distributor and a period.
 */
@RestController
@RequestMapping("/api/v1/bordereaux")
@RequiredArgsConstructor
@Validated
@Tag(name = "Bordereaux", description = "Bordereau for a distributor and a period")
public class BordereauController {

    private final BordereauService bordereauService;

    @PostMapping("/filter")
    @Operation(summary = "Filter bordereaux", description = "Retrieve a page of bordereaux matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<BordereauDTO>>> filterBordereaux(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<BordereauDTO> filterRequest) {
        return bordereauService.filterBordereaux(filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create Bordereau", description = "Create a Bordereau")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Bordereau created", content = @Content(schema = @Schema(implementation = BordereauDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<BordereauDTO>> createBordereau(
            @Parameter(description = "Bordereau to create", required = true)
            @Valid @RequestBody BordereauDTO dto) {
        return bordereauService.createBordereau(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{bordereauId}")
    @Operation(summary = "Get Bordereau by id", description = "Retrieve one Bordereau")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bordereau found", content = @Content(schema = @Schema(implementation = BordereauDTO.class))),
        @ApiResponse(responseCode = "404", description = "Bordereau not found", content = @Content)
    })
    public Mono<ResponseEntity<BordereauDTO>> getBordereauById(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId) {
        return bordereauService.getBordereauById(bordereauId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{bordereauId}")
    @Operation(summary = "Update Bordereau", description = "Update an existing Bordereau")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bordereau updated", content = @Content(schema = @Schema(implementation = BordereauDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bordereau not found", content = @Content)
    })
    public Mono<ResponseEntity<BordereauDTO>> updateBordereau(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId,
            @Parameter(description = "Updated Bordereau", required = true)
            @Valid @RequestBody BordereauDTO dto) {
        return bordereauService.updateBordereau(bordereauId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{bordereauId}")
    @Operation(summary = "Delete Bordereau", description = "Delete a Bordereau")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Bordereau deleted"),
        @ApiResponse(responseCode = "404", description = "Bordereau not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteBordereau(
            @Parameter(description = "Unique identifier of the Bordereau", required = true)
            @PathVariable UUID bordereauId) {
        return bordereauService.deleteBordereau(bordereauId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
