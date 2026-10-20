-- Flyway V3: implicit varchar casts for enum parameters

CREATE CAST (varchar AS schedule_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS instalment_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS commission_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS commission_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS refund_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS disbursement_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS bordereau_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS bordereau_line_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS audit_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS credit_control_status_enum) WITH INOUT AS IMPLICIT;
