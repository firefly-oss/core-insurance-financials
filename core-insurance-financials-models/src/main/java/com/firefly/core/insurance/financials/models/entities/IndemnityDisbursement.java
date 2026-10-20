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

import com.firefly.core.insurance.financials.interfaces.enums.DisbursementStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Indemnity record pointing at a claim settlement and, once paid, a payment order.
 * Maps to the 'indemnity_disbursement' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("indemnity_disbursement")
public class IndemnityDisbursement {

    @Id
    @Column("indemnity_disbursement_id")
    private UUID indemnityDisbursementId;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("claim_id")
    private UUID claimId;

    @Column("claim_settlement_id")
    private UUID claimSettlementId;

    @Column("payee_party_id")
    private UUID payeePartyId;

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;

    @Column("disbursement_status")
    private DisbursementStatus disbursementStatus;

    @Column("payment_order_id")
    private UUID paymentOrderId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
