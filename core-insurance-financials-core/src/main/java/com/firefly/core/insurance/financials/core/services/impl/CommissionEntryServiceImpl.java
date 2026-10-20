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


package com.firefly.core.insurance.financials.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import com.firefly.core.insurance.financials.core.mappers.CommissionEntryMapper;
import com.firefly.core.insurance.financials.core.services.CommissionEntryService;
import com.firefly.core.insurance.financials.interfaces.dtos.CommissionEntryDTO;
import com.firefly.core.insurance.financials.models.entities.CommissionEntry;
import com.firefly.core.insurance.financials.models.repositories.CommissionEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CommissionEntryServiceImpl implements CommissionEntryService {

    @Autowired
    private CommissionEntryRepository repository;

    @Autowired
    private CommissionEntryMapper mapper;

    @Override
    public Mono<PaginationResponse<CommissionEntryDTO>> filterCommissionEntries(UUID premiumScheduleId, FilterRequest<CommissionEntryDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new CommissionEntryDTO());
        }
        filterRequest.getFilters().setPremiumScheduleId(premiumScheduleId);
        return FilterUtils.createFilter(CommissionEntry.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<CommissionEntryDTO> createCommissionEntry(UUID premiumScheduleId, CommissionEntryDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setPremiumScheduleId(premiumScheduleId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CommissionEntryDTO> updateCommissionEntry(UUID premiumScheduleId, UUID commissionEntryId, CommissionEntryDTO dto) {
        return repository.findById(commissionEntryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("CommissionEntry", commissionEntryId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "CommissionEntry with id '" + commissionEntryId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteCommissionEntry(UUID premiumScheduleId, UUID commissionEntryId) {
        return repository.findById(commissionEntryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("CommissionEntry", commissionEntryId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "CommissionEntry with id '" + commissionEntryId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return repository.deleteById(commissionEntryId);
                });
    }

    @Override
    public Mono<CommissionEntryDTO> getCommissionEntryById(UUID premiumScheduleId, UUID commissionEntryId) {
        return repository.findById(commissionEntryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("CommissionEntry", commissionEntryId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "CommissionEntry with id '" + commissionEntryId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
