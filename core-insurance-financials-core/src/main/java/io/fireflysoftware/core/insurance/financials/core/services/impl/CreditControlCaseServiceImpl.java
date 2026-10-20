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


package io.fireflysoftware.core.insurance.financials.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import io.fireflysoftware.core.insurance.financials.core.mappers.CreditControlCaseMapper;
import io.fireflysoftware.core.insurance.financials.core.services.CreditControlCaseService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.CreditControlCaseDTO;
import io.fireflysoftware.core.insurance.financials.models.entities.CreditControlCase;
import io.fireflysoftware.core.insurance.financials.models.repositories.CreditControlCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CreditControlCaseServiceImpl implements CreditControlCaseService {

    @Autowired
    private CreditControlCaseRepository repository;

    @Autowired
    private CreditControlCaseMapper mapper;

    @Override
    public Mono<PaginationResponse<CreditControlCaseDTO>> filterCreditControlCases(UUID premiumScheduleId, FilterRequest<CreditControlCaseDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new CreditControlCaseDTO());
        }
        filterRequest.getFilters().setPremiumScheduleId(premiumScheduleId);
        return FilterUtils.createFilter(CreditControlCase.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<CreditControlCaseDTO> createCreditControlCase(UUID premiumScheduleId, CreditControlCaseDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setPremiumScheduleId(premiumScheduleId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CreditControlCaseDTO> updateCreditControlCase(UUID premiumScheduleId, UUID creditControlCaseId, CreditControlCaseDTO dto) {
        return repository.findById(creditControlCaseId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("CreditControlCase", creditControlCaseId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "CreditControlCase with id '" + creditControlCaseId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteCreditControlCase(UUID premiumScheduleId, UUID creditControlCaseId) {
        return repository.findById(creditControlCaseId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("CreditControlCase", creditControlCaseId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "CreditControlCase with id '" + creditControlCaseId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return repository.deleteById(creditControlCaseId);
                });
    }

    @Override
    public Mono<CreditControlCaseDTO> getCreditControlCaseById(UUID premiumScheduleId, UUID creditControlCaseId) {
        return repository.findById(creditControlCaseId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("CreditControlCase", creditControlCaseId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "CreditControlCase with id '" + creditControlCaseId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
