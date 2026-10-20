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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.financials.interfaces.enums.InstalmentStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * One instalment on a premium schedule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumInstalmentDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID premiumInstalmentId;

    @FilterableId
    @NotNull(message = "PremiumSchedule id is required")
    private UUID premiumScheduleId;

    @NotNull(message = "sequenceNumber is required")
    private Integer sequenceNumber;

    @NotNull(message = "dueDate is required")
    private LocalDate dueDate;

    @NotNull(message = "amount is required")
    private BigDecimal amount;

    @NotNull(message = "instalmentStatus is required")
    private InstalmentStatus instalmentStatus;

    /** Logical reference: core-banking-payments payment order. This service does not own that record. */
    @FilterableId
    private UUID paymentOrderId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
