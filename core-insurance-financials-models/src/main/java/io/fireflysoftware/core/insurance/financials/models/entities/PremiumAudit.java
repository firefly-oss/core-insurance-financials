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


package io.fireflysoftware.core.insurance.financials.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.financials.interfaces.enums.AuditStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Premium audit adjustment from declared exposure. The rating figure is recorded, not calculated here..
 * Maps to the 'premium_audit' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("premium_audit")
public class PremiumAudit {

    @Id
    @Column("premium_audit_id")
    private UUID premiumAuditId;

    @Column("premium_schedule_id")
    private UUID premiumScheduleId;

    @Column("policy_id")
    private UUID policyId;

    @Column("declared_exposure")
    private BigDecimal declaredExposure;

    @Column("audited_exposure")
    private BigDecimal auditedExposure;

    @Column("adjustment_amount")
    private BigDecimal adjustmentAmount;

    @Column("currency")
    private String currency;

    @Column("audit_status")
    private AuditStatus auditStatus;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
