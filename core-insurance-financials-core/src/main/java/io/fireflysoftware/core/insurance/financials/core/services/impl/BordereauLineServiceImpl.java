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
import io.fireflysoftware.core.insurance.financials.core.mappers.BordereauLineMapper;
import io.fireflysoftware.core.insurance.financials.core.services.BordereauLineService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.BordereauLineDTO;
import io.fireflysoftware.core.insurance.financials.models.entities.BordereauLine;
import io.fireflysoftware.core.insurance.financials.models.repositories.BordereauLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class BordereauLineServiceImpl implements BordereauLineService {

    @Autowired
    private BordereauLineRepository repository;

    @Autowired
    private BordereauLineMapper mapper;

    @Override
    public Mono<PaginationResponse<BordereauLineDTO>> filterBordereauLines(UUID bordereauId, FilterRequest<BordereauLineDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new BordereauLineDTO());
        }
        filterRequest.getFilters().setBordereauId(bordereauId);
        return FilterUtils.createFilter(BordereauLine.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<BordereauLineDTO> createBordereauLine(UUID bordereauId, BordereauLineDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setBordereauId(bordereauId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<BordereauLineDTO> updateBordereauLine(UUID bordereauId, UUID bordereauLineId, BordereauLineDTO dto) {
        return repository.findById(bordereauLineId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("BordereauLine", bordereauLineId.toString())))
                .flatMap(existing -> {
                    if (!bordereauId.equals(existing.getBordereauId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "BordereauLine with id '" + bordereauLineId + "' does not belong to bordereau '" + bordereauId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteBordereauLine(UUID bordereauId, UUID bordereauLineId) {
        return repository.findById(bordereauLineId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("BordereauLine", bordereauLineId.toString())))
                .flatMap(existing -> {
                    if (!bordereauId.equals(existing.getBordereauId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "BordereauLine with id '" + bordereauLineId + "' does not belong to bordereau '" + bordereauId + "'"));
                    }
                    return repository.deleteById(bordereauLineId);
                });
    }

    @Override
    public Mono<BordereauLineDTO> getBordereauLineById(UUID bordereauId, UUID bordereauLineId) {
        return repository.findById(bordereauLineId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("BordereauLine", bordereauLineId.toString())))
                .flatMap(existing -> {
                    if (!bordereauId.equals(existing.getBordereauId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "BordereauLine with id '" + bordereauLineId + "' does not belong to bordereau '" + bordereauId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
