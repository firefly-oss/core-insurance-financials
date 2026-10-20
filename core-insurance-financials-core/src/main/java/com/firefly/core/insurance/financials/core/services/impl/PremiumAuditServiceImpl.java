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
import com.firefly.core.insurance.financials.core.mappers.PremiumAuditMapper;
import com.firefly.core.insurance.financials.core.services.PremiumAuditService;
import com.firefly.core.insurance.financials.interfaces.dtos.PremiumAuditDTO;
import com.firefly.core.insurance.financials.models.entities.PremiumAudit;
import com.firefly.core.insurance.financials.models.repositories.PremiumAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class PremiumAuditServiceImpl implements PremiumAuditService {

    @Autowired
    private PremiumAuditRepository repository;

    @Autowired
    private PremiumAuditMapper mapper;

    @Override
    public Mono<PaginationResponse<PremiumAuditDTO>> filterPremiumAudits(UUID premiumScheduleId, FilterRequest<PremiumAuditDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new PremiumAuditDTO());
        }
        filterRequest.getFilters().setPremiumScheduleId(premiumScheduleId);
        return FilterUtils.createFilter(PremiumAudit.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<PremiumAuditDTO> createPremiumAudit(UUID premiumScheduleId, PremiumAuditDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setPremiumScheduleId(premiumScheduleId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PremiumAuditDTO> updatePremiumAudit(UUID premiumScheduleId, UUID premiumAuditId, PremiumAuditDTO dto) {
        return repository.findById(premiumAuditId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumAudit", premiumAuditId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumAudit with id '" + premiumAuditId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deletePremiumAudit(UUID premiumScheduleId, UUID premiumAuditId) {
        return repository.findById(premiumAuditId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumAudit", premiumAuditId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumAudit with id '" + premiumAuditId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return repository.deleteById(premiumAuditId);
                });
    }

    @Override
    public Mono<PremiumAuditDTO> getPremiumAuditById(UUID premiumScheduleId, UUID premiumAuditId) {
        return repository.findById(premiumAuditId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumAudit", premiumAuditId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumAudit with id '" + premiumAuditId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
