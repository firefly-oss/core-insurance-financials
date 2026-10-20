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


package com.firefly.core.insurance.financials.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.financials.interfaces.enums.CreditControlStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Arrears follow-up and lapse notice against a premium schedule.
 * Maps to the 'credit_control_case' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("credit_control_case")
public class CreditControlCase {

    @Id
    @Column("credit_control_case_id")
    private UUID creditControlCaseId;

    @Column("premium_schedule_id")
    private UUID premiumScheduleId;

    @Column("case_status")
    private CreditControlStatus caseStatus;

    @Column("arrears_amount")
    private BigDecimal arrearsAmount;

    @Column("currency")
    private String currency;

    @Column("opened_at")
    private LocalDateTime openedAt;

    @Column("lapse_notice_at")
    private LocalDateTime lapseNoticeAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
