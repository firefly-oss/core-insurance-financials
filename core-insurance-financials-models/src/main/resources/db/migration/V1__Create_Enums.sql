-- Flyway V1: enum types for core-insurance-financials

CREATE TYPE schedule_status_enum AS ENUM (
    'ACTIVE',
    'COMPLETED',
    'CANCELLED'
);

CREATE TYPE instalment_status_enum AS ENUM (
    'SCHEDULED',
    'DUE',
    'COLLECTED',
    'OVERDUE',
    'WAIVED',
    'REFUNDED'
);

CREATE TYPE commission_type_enum AS ENUM (
    'ACQUISITION',
    'RENEWAL',
    'OVERRIDE'
);

CREATE TYPE commission_status_enum AS ENUM (
    'ACCRUED',
    'PAYABLE',
    'PAID',
    'CLAWED_BACK'
);

CREATE TYPE refund_status_enum AS ENUM (
    'REQUESTED',
    'APPROVED',
    'PAID',
    'REJECTED'
);

CREATE TYPE disbursement_status_enum AS ENUM (
    'RECORDED',
    'SUBMITTED',
    'SETTLED',
    'FAILED'
);

CREATE TYPE bordereau_status_enum AS ENUM (
    'DRAFT',
    'SUBMITTED',
    'ACCEPTED',
    'REJECTED'
);

CREATE TYPE bordereau_line_type_enum AS ENUM (
    'PREMIUM',
    'COMMISSION',
    'CLAIM',
    'REFUND'
);

CREATE TYPE audit_status_enum AS ENUM (
    'DRAFT',
    'PROPOSED',
    'APPROVED',
    'REJECTED'
);

CREATE TYPE credit_control_status_enum AS ENUM (
    'OPEN',
    'REMINDED',
    'LAPSE_NOTICE',
    'LAPSED',
    'CURED'
);

