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
import io.fireflysoftware.core.insurance.financials.core.mappers.BordereauMapper;
import io.fireflysoftware.core.insurance.financials.core.services.BordereauService;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.BordereauDTO;
import io.fireflysoftware.core.insurance.financials.models.entities.Bordereau;
import io.fireflysoftware.core.insurance.financials.models.repositories.BordereauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class BordereauServiceImpl implements BordereauService {

    @Autowired
    private BordereauRepository repository;

    @Autowired
    private BordereauMapper mapper;

    @Override
    public Mono<PaginationResponse<BordereauDTO>> filterBordereaux(FilterRequest<BordereauDTO> filterRequest) {
        return FilterUtils.createFilter(Bordereau.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<BordereauDTO> createBordereau(BordereauDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<BordereauDTO> updateBordereau(UUID bordereauId, BordereauDTO dto) {
        return repository.findById(bordereauId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Bordereau", bordereauId.toString())))
                .flatMap(existing -> {
                    mapper.updateEntityFromDto(dto, existing);
                    existing.setBordereauId(bordereauId);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteBordereau(UUID bordereauId) {
        return repository.findById(bordereauId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Bordereau", bordereauId.toString())))
                .flatMap(existing -> repository.deleteById(bordereauId));
    }

    @Override
    public Mono<BordereauDTO> getBordereauById(UUID bordereauId) {
        return repository.findById(bordereauId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Bordereau", bordereauId.toString())))
                .map(mapper::toDTO);
    }
}
