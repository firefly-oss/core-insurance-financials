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
import com.firefly.core.insurance.financials.core.mappers.PremiumRefundMapper;
import com.firefly.core.insurance.financials.core.services.PremiumRefundService;
import com.firefly.core.insurance.financials.interfaces.dtos.PremiumRefundDTO;
import com.firefly.core.insurance.financials.models.entities.PremiumRefund;
import com.firefly.core.insurance.financials.models.repositories.PremiumRefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class PremiumRefundServiceImpl implements PremiumRefundService {

    @Autowired
    private PremiumRefundRepository repository;

    @Autowired
    private PremiumRefundMapper mapper;

    @Override
    public Mono<PaginationResponse<PremiumRefundDTO>> filterPremiumRefunds(UUID premiumScheduleId, FilterRequest<PremiumRefundDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new PremiumRefundDTO());
        }
        filterRequest.getFilters().setPremiumScheduleId(premiumScheduleId);
        return FilterUtils.createFilter(PremiumRefund.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<PremiumRefundDTO> createPremiumRefund(UUID premiumScheduleId, PremiumRefundDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setPremiumScheduleId(premiumScheduleId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PremiumRefundDTO> updatePremiumRefund(UUID premiumScheduleId, UUID premiumRefundId, PremiumRefundDTO dto) {
        return repository.findById(premiumRefundId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumRefund", premiumRefundId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumRefund with id '" + premiumRefundId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deletePremiumRefund(UUID premiumScheduleId, UUID premiumRefundId) {
        return repository.findById(premiumRefundId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumRefund", premiumRefundId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumRefund with id '" + premiumRefundId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return repository.deleteById(premiumRefundId);
                });
    }

    @Override
    public Mono<PremiumRefundDTO> getPremiumRefundById(UUID premiumScheduleId, UUID premiumRefundId) {
        return repository.findById(premiumRefundId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumRefund", premiumRefundId.toString())))
                .flatMap(existing -> {
                    if (!premiumScheduleId.equals(existing.getPremiumScheduleId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "PremiumRefund with id '" + premiumRefundId + "' does not belong to premiumschedule '" + premiumScheduleId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
