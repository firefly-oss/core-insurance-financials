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


package com.firefly.core.insurance.financials.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.financials.interfaces.dtos.PremiumInstalmentDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for one instalment on a premium schedule.
 */
public interface PremiumInstalmentService {

    /**
     * Filters premiumInstalments with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of PremiumInstalmentDTO
     */
    Mono<PaginationResponse<PremiumInstalmentDTO>> filterPremiumInstalments(UUID premiumScheduleId, FilterRequest<PremiumInstalmentDTO> filterRequest);

    /**
     * Creates a PremiumInstalment.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<PremiumInstalmentDTO> createPremiumInstalment(UUID premiumScheduleId, PremiumInstalmentDTO dto);

    /**
     * Updates a PremiumInstalment.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<PremiumInstalmentDTO> updatePremiumInstalment(UUID premiumScheduleId, UUID premiumInstalmentId, PremiumInstalmentDTO dto);

    /**
     * Deletes a PremiumInstalment.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deletePremiumInstalment(UUID premiumScheduleId, UUID premiumInstalmentId);

    /**
     * Loads a PremiumInstalment by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<PremiumInstalmentDTO> getPremiumInstalmentById(UUID premiumScheduleId, UUID premiumInstalmentId);
}
