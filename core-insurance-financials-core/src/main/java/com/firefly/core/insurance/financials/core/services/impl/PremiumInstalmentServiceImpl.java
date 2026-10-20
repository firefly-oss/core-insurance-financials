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
import com.firefly.core.insurance.financials.core.mappers.PremiumInstalmentMapper;
import com.firefly.core.insurance.financials.core.services.PremiumInstalmentService;
import com.firefly.core.insurance.financials.interfaces.dtos.PremiumInstalmentDTO;
import com.firefly.core.insurance.financials.models.entities.PremiumInstalment;
import com.firefly.core.insurance.financials.models.repositories.PremiumInstalmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class PremiumInstalmentServiceImpl implements PremiumInstalmentService {

    @Autowired
    private PremiumInstalmentRepository repository;

    @Autowired
    private PremiumInstalmentMapper mapper;

    @Override
    public Mono<PaginationResponse<PremiumInstalmentDTO>> filterPremiumInstalments(UUID premiumScheduleId, FilterRequest<PremiumInstalmentDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new PremiumInstalmentDTO());
        }
        filterRequest.getFilters().setPremiumScheduleId(premiumScheduleId);
        return FilterUtils.createFilter(PremiumInstalment.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<PremiumInstalmentDTO> createPremiumInstalment(UUID premiumScheduleId, PremiumInstalmentDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setPremiumScheduleId(premiumScheduleId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PremiumInstalmentDTO> updatePremiumInstalment(UUID premiumScheduleId, UUID premiumInstalmentId, PremiumInstalmentDTO dto) {
        return repository.findById(premiumInstalmentId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumInstalment", premiumInstalmentId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumInstalment with id '" + premiumInstalmentId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deletePremiumInstalment(UUID premiumScheduleId, UUID premiumInstalmentId) {
        return repository.findById(premiumInstalmentId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumInstalment", premiumInstalmentId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumInstalment with id '" + premiumInstalmentId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return repository.deleteById(premiumInstalmentId);
                });
    }

    @Override
    public Mono<PremiumInstalmentDTO> getPremiumInstalmentById(UUID premiumScheduleId, UUID premiumInstalmentId) {
        return repository.findById(premiumInstalmentId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumInstalment", premiumInstalmentId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumInstalment with id '" + premiumInstalmentId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
