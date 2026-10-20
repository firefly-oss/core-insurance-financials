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


package io.fireflysoftware.core.insurance.financials.interfaces.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.financials.interfaces.enums.BordereauStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Bordereau for a distributor and a period.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BordereauDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID bordereauId;

    /** Logical reference: Firefly deployment tenant. This service does not own that record. */
    @FilterableId
    @NotNull(message = "tenantId is required")
    private UUID tenantId;

    /** Logical reference: core-common-distributor-mgmt distributor. This service does not own that record. */
    @FilterableId
    @NotNull(message = "distributorId is required")
    private UUID distributorId;

    @NotNull(message = "periodStart is required")
    private LocalDate periodStart;

    @NotNull(message = "periodEnd is required")
    private LocalDate periodEnd;

    @NotNull(message = "bordereauStatus is required")
    private BordereauStatus bordereauStatus;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
