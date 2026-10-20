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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.financials.interfaces.enums.AuditStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Premium audit adjustment from declared exposure. The rating figure is recorded, not calculated here..
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumAuditDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID premiumAuditId;

    @FilterableId
    @NotNull(message = "PremiumSchedule id is required")
    private UUID premiumScheduleId;

    /** Logical reference: core-insurance-policies policy. This service does not own that record. */
    @FilterableId
    @NotNull(message = "policyId is required")
    private UUID policyId;

    private BigDecimal declaredExposure;

    private BigDecimal auditedExposure;

    @NotNull(message = "adjustmentAmount is required")
    private BigDecimal adjustmentAmount;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    @NotNull(message = "auditStatus is required")
    private AuditStatus auditStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
