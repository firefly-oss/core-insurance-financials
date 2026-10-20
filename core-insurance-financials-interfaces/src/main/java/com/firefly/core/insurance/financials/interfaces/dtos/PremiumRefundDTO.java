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


package com.firefly.core.insurance.financials.interfaces.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.financials.interfaces.enums.RefundStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Refund recorded against a premium schedule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumRefundDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID premiumRefundId;

    @FilterableId
    @NotNull(message = "PremiumSchedule id is required")
    private UUID premiumScheduleId;

    /** Logical reference: core-insurance-policies policy. This service does not own that record. */
    @FilterableId
    @NotNull(message = "policyId is required")
    private UUID policyId;

    @NotNull(message = "amount is required")
    private BigDecimal amount;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    @Size(max = 4000, message = "reason must not exceed 4000 characters")
    private String reason;

    @NotNull(message = "refundStatus is required")
    private RefundStatus refundStatus;

    /** Logical reference: core-banking-payments payment order. This service does not own that record. */
    @FilterableId
    private UUID paymentOrderId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
