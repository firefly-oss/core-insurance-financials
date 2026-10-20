-- Flyway V2: tables for core-insurance-financials

-- Premium schedule for a policy
CREATE TABLE premium_schedule (
    premium_schedule_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    policy_id UUID NOT NULL,
    contract_id UUID NULL,
    currency VARCHAR(3) NOT NULL,
    schedule_status schedule_status_enum NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- One instalment on a premium schedule
CREATE TABLE premium_instalment (
    premium_instalment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    premium_schedule_id UUID NOT NULL REFERENCES premium_schedule (premium_schedule_id) ON DELETE CASCADE,
    sequence_number INTEGER NOT NULL,
    due_date DATE NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    instalment_status instalment_status_enum NOT NULL,
    payment_order_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Commission accrued for a party or distributor against a premium schedule
CREATE TABLE commission_entry (
    commission_entry_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    premium_schedule_id UUID NOT NULL REFERENCES premium_schedule (premium_schedule_id) ON DELETE CASCADE,
    party_id UUID NOT NULL,
    distributor_id UUID NULL,
    commission_type commission_type_enum NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    commission_status commission_status_enum NOT NULL,
    payment_order_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Refund recorded against a premium schedule
CREATE TABLE premium_refund (
    premium_refund_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    premium_schedule_id UUID NOT NULL REFERENCES premium_schedule (premium_schedule_id) ON DELETE CASCADE,
    policy_id UUID NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    reason TEXT NULL,
    refund_status refund_status_enum NOT NULL,
    payment_order_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indemnity record pointing at a claim settlement and, once paid, a payment order
CREATE TABLE indemnity_disbursement (
    indemnity_disbursement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    claim_id UUID NOT NULL,
    claim_settlement_id UUID NOT NULL,
    payee_party_id UUID NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    disbursement_status disbursement_status_enum NOT NULL,
    payment_order_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Bordereau for a distributor and a period
CREATE TABLE bordereau (
    bordereau_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    distributor_id UUID NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    bordereau_status bordereau_status_enum NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- One premium, commission, claim, or refund line on a bordereau
CREATE TABLE bordereau_line (
    bordereau_line_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bordereau_id UUID NOT NULL REFERENCES bordereau (bordereau_id) ON DELETE CASCADE,
    line_type bordereau_line_type_enum NOT NULL,
    policy_id UUID NULL,
    claim_id UUID NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    reference VARCHAR(128) NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Premium audit adjustment from declared exposure. The rating figure is recorded, not calculated here.
CREATE TABLE premium_audit (
    premium_audit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    premium_schedule_id UUID NOT NULL REFERENCES premium_schedule (premium_schedule_id) ON DELETE CASCADE,
    policy_id UUID NOT NULL,
    declared_exposure DECIMAL(19, 4) NULL,
    audited_exposure DECIMAL(19, 4) NULL,
    adjustment_amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    audit_status audit_status_enum NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Arrears follow-up and lapse notice against a premium schedule
CREATE TABLE credit_control_case (
    credit_control_case_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    premium_schedule_id UUID NOT NULL REFERENCES premium_schedule (premium_schedule_id) ON DELETE CASCADE,
    case_status credit_control_status_enum NOT NULL,
    arrears_amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    opened_at TIMESTAMP WITH TIME ZONE NOT NULL,
    lapse_notice_at TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_premium_schedule_tenant_id ON premium_schedule (tenant_id);
CREATE INDEX idx_premium_schedule_policy_id ON premium_schedule (policy_id);
CREATE INDEX idx_premium_schedule_contract_id ON premium_schedule (contract_id);
CREATE INDEX idx_premium_instalment_premium_schedule_id ON premium_instalment (premium_schedule_id);
CREATE INDEX idx_premium_instalment_payment_order_id ON premium_instalment (payment_order_id);
CREATE INDEX idx_commission_entry_premium_schedule_id ON commission_entry (premium_schedule_id);
CREATE INDEX idx_commission_entry_party_id ON commission_entry (party_id);
CREATE INDEX idx_commission_entry_distributor_id ON commission_entry (distributor_id);
CREATE INDEX idx_commission_entry_payment_order_id ON commission_entry (payment_order_id);
CREATE INDEX idx_premium_refund_premium_schedule_id ON premium_refund (premium_schedule_id);
CREATE INDEX idx_premium_refund_policy_id ON premium_refund (policy_id);
CREATE INDEX idx_premium_refund_payment_order_id ON premium_refund (payment_order_id);
CREATE INDEX idx_indemnity_disbursement_tenant_id ON indemnity_disbursement (tenant_id);
CREATE INDEX idx_indemnity_disbursement_claim_id ON indemnity_disbursement (claim_id);
CREATE INDEX idx_indemnity_disbursement_claim_settlement_id ON indemnity_disbursement (claim_settlement_id);
CREATE INDEX idx_indemnity_disbursement_payee_party_id ON indemnity_disbursement (payee_party_id);
CREATE INDEX idx_indemnity_disbursement_payment_order_id ON indemnity_disbursement (payment_order_id);
CREATE INDEX idx_bordereau_tenant_id ON bordereau (tenant_id);
CREATE INDEX idx_bordereau_distributor_id ON bordereau (distributor_id);
CREATE INDEX idx_bordereau_line_bordereau_id ON bordereau_line (bordereau_id);
CREATE INDEX idx_bordereau_line_policy_id ON bordereau_line (policy_id);
CREATE INDEX idx_bordereau_line_claim_id ON bordereau_line (claim_id);
CREATE INDEX idx_premium_audit_premium_schedule_id ON premium_audit (premium_schedule_id);
CREATE INDEX idx_premium_audit_policy_id ON premium_audit (policy_id);
CREATE INDEX idx_credit_control_case_premium_schedule_id ON credit_control_case (premium_schedule_id);
COMMENT ON COLUMN premium_schedule.tenant_id IS 'Logical reference: Firefly deployment tenant. No foreign key across services.';
COMMENT ON COLUMN premium_schedule.policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
COMMENT ON COLUMN premium_schedule.contract_id IS 'Logical reference: core-common-contract-mgmt contract. No foreign key across services.';
COMMENT ON COLUMN premium_instalment.payment_order_id IS 'Logical reference: core-banking-payments payment order. No foreign key across services.';
COMMENT ON COLUMN commission_entry.party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN commission_entry.distributor_id IS 'Logical reference: core-common-distributor-mgmt distributor. No foreign key across services.';
COMMENT ON COLUMN commission_entry.payment_order_id IS 'Logical reference: core-banking-payments payment order. No foreign key across services.';
COMMENT ON COLUMN premium_refund.policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
COMMENT ON COLUMN premium_refund.payment_order_id IS 'Logical reference: core-banking-payments payment order. No foreign key across services.';
COMMENT ON COLUMN indemnity_disbursement.tenant_id IS 'Logical reference: Firefly deployment tenant. No foreign key across services.';
COMMENT ON COLUMN indemnity_disbursement.claim_id IS 'Logical reference: core-insurance-claims claim. No foreign key across services.';
COMMENT ON COLUMN indemnity_disbursement.claim_settlement_id IS 'Logical reference: core-insurance-claims settlement. No foreign key across services.';
COMMENT ON COLUMN indemnity_disbursement.payee_party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN indemnity_disbursement.payment_order_id IS 'Logical reference: core-banking-payments payment order. No foreign key across services.';
COMMENT ON COLUMN bordereau.tenant_id IS 'Logical reference: Firefly deployment tenant. No foreign key across services.';
COMMENT ON COLUMN bordereau.distributor_id IS 'Logical reference: core-common-distributor-mgmt distributor. No foreign key across services.';
COMMENT ON COLUMN bordereau_line.policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
COMMENT ON COLUMN bordereau_line.claim_id IS 'Logical reference: core-insurance-claims claim. No foreign key across services.';
COMMENT ON COLUMN premium_audit.policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
