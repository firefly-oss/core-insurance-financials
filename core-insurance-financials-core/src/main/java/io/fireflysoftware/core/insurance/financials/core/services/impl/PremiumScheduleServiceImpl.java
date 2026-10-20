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
import io.fireflysoftware.core.insurance.financials.core.mappers.PremiumScheduleMapper;
import io.fireflysoftware.core.insurance.financials.core.services.PremiumScheduleService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.PremiumScheduleDTO;
import io.fireflysoftware.core.insurance.financials.models.entities.PremiumSchedule;
import io.fireflysoftware.core.insurance.financials.models.repositories.PremiumScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class PremiumScheduleServiceImpl implements PremiumScheduleService {

    @Autowired
    private PremiumScheduleRepository repository;

    @Autowired
    private PremiumScheduleMapper mapper;

    @Override
    public Mono<PaginationResponse<PremiumScheduleDTO>> filterPremiumSchedules(FilterRequest<PremiumScheduleDTO> filterRequest) {
        return FilterUtils.createFilter(PremiumSchedule.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<PremiumScheduleDTO> createPremiumSchedule(PremiumScheduleDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PremiumScheduleDTO> updatePremiumSchedule(UUID premiumScheduleId, PremiumScheduleDTO dto) {
        return repository.findById(premiumScheduleId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumSchedule", premiumScheduleId.toString())))
                .flatMap(existing -> {
                    mapper.updateEntityFromDto(dto, existing);
                    existing.setPremiumScheduleId(premiumScheduleId);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deletePremiumSchedule(UUID premiumScheduleId) {
        return repository.findById(premiumScheduleId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumSchedule", premiumScheduleId.toString())))
                .flatMap(existing -> repository.deleteById(premiumScheduleId));
    }

    @Override
    public Mono<PremiumScheduleDTO> getPremiumScheduleById(UUID premiumScheduleId) {
        return repository.findById(premiumScheduleId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("PremiumSchedule", premiumScheduleId.toString())))
                .map(mapper::toDTO);
    }
}
