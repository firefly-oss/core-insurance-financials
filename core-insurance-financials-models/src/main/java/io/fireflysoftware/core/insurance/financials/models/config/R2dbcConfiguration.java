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


package io.fireflysoftware.core.insurance.financials.models.config;

import io.fireflysoftware.core.insurance.financials.interfaces.enums.ScheduleStatus;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.InstalmentStatus;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.CommissionType;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.CommissionStatus;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.RefundStatus;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.DisbursementStatus;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.BordereauStatus;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.BordereauLineType;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.AuditStatus;
import io.fireflysoftware.core.insurance.financials.interfaces.enums.CreditControlStatus;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers every PostgreSQL enum used by this service with the R2DBC codec.
 * Without this, the driver binds enum parameters as varchar and Postgres rejects the cast.
 */
@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Value("${DB_HOST:localhost}")
    private String host;

    @Value("${DB_PORT:5432}")
    private int port;

    @Value("${DB_NAME:postgres}")
    private String database;

    @Value("${DB_USERNAME:postgres}")
    private String username;

    @Value("${DB_PASSWORD:postgres}")
    private String password;

    @Value("${DB_SSL_MODE:disable}")
    private String sslMode;

    @WritingConverter
    static class ScheduleStatusConverter implements Converter<ScheduleStatus, ScheduleStatus> {
        @Override
        public ScheduleStatus convert(ScheduleStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class InstalmentStatusConverter implements Converter<InstalmentStatus, InstalmentStatus> {
        @Override
        public InstalmentStatus convert(InstalmentStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class CommissionTypeConverter implements Converter<CommissionType, CommissionType> {
        @Override
        public CommissionType convert(CommissionType source) {
            return source;
        }
    }

    @WritingConverter
    static class CommissionStatusConverter implements Converter<CommissionStatus, CommissionStatus> {
        @Override
        public CommissionStatus convert(CommissionStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class RefundStatusConverter implements Converter<RefundStatus, RefundStatus> {
        @Override
        public RefundStatus convert(RefundStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class DisbursementStatusConverter implements Converter<DisbursementStatus, DisbursementStatus> {
        @Override
        public DisbursementStatus convert(DisbursementStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class BordereauStatusConverter implements Converter<BordereauStatus, BordereauStatus> {
        @Override
        public BordereauStatus convert(BordereauStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class BordereauLineTypeConverter implements Converter<BordereauLineType, BordereauLineType> {
        @Override
        public BordereauLineType convert(BordereauLineType source) {
            return source;
        }
    }

    @WritingConverter
    static class AuditStatusConverter implements Converter<AuditStatus, AuditStatus> {
        @Override
        public AuditStatus convert(AuditStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class CreditControlStatusConverter implements Converter<CreditControlStatus, CreditControlStatus> {
        @Override
        public CreditControlStatus convert(CreditControlStatus source) {
            return source;
        }
    }

    @Override
    protected List<Object> getCustomConverters() {
        List<Object> converters = new ArrayList<>();
        converters.add(new ScheduleStatusConverter());
        converters.add(new InstalmentStatusConverter());
        converters.add(new CommissionTypeConverter());
        converters.add(new CommissionStatusConverter());
        converters.add(new RefundStatusConverter());
        converters.add(new DisbursementStatusConverter());
        converters.add(new BordereauStatusConverter());
        converters.add(new BordereauLineTypeConverter());
        converters.add(new AuditStatusConverter());
        converters.add(new CreditControlStatusConverter());
        return converters;
    }

    @Bean
    @Primary
    @Override
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .username(username)
                        .password(password)
                        .database(database)
                        .sslMode(SSLMode.valueOf(sslMode.toUpperCase()))
                        .codecRegistrar(EnumCodec.builder()
                                .withEnum("schedule_status_enum", ScheduleStatus.class)
                                .withEnum("instalment_status_enum", InstalmentStatus.class)
                                .withEnum("commission_type_enum", CommissionType.class)
                                .withEnum("commission_status_enum", CommissionStatus.class)
                                .withEnum("refund_status_enum", RefundStatus.class)
                                .withEnum("disbursement_status_enum", DisbursementStatus.class)
                                .withEnum("bordereau_status_enum", BordereauStatus.class)
                                .withEnum("bordereau_line_type_enum", BordereauLineType.class)
                                .withEnum("audit_status_enum", AuditStatus.class)
                                .withEnum("credit_control_status_enum", CreditControlStatus.class)
                                .build())
                        .build()
        );
    }
}
