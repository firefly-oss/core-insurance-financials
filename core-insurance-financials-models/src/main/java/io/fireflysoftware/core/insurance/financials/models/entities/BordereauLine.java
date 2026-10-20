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

import io.fireflysoftware.core.insurance.financials.interfaces.enums.BordereauLineType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * One premium, commission, claim, or refund line on a bordereau.
 * Maps to the 'bordereau_line' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("bordereau_line")
public class BordereauLine {

    @Id
    @Column("bordereau_line_id")
    private UUID bordereauLineId;

    @Column("bordereau_id")
    private UUID bordereauId;

    @Column("line_type")
    private BordereauLineType lineType;

    @Column("policy_id")
    private UUID policyId;

    @Column("claim_id")
    private UUID claimId;

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;

    @Column("reference")
    private String reference;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
