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


package io.fireflysoftware.core.insurance.financials.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.PremiumScheduleDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for premium schedule for a policy.
 */
public interface PremiumScheduleService {

    /**
     * Filters premiumSchedules with the given criteria.
     *
     * @param filterRequest filter criteria
     * @return a page of PremiumScheduleDTO
     */
    Mono<PaginationResponse<PremiumScheduleDTO>> filterPremiumSchedules(FilterRequest<PremiumScheduleDTO> filterRequest);

    /**
     * Creates a PremiumSchedule.
     *
     * @param dto values to store
     * @return the stored DTO
     */
    Mono<PremiumScheduleDTO> createPremiumSchedule(PremiumScheduleDTO dto);

    /**
     * Updates a PremiumSchedule.
     *
     * @param id and dto
     * @return the updated DTO
     */
    Mono<PremiumScheduleDTO> updatePremiumSchedule(UUID premiumScheduleId, PremiumScheduleDTO dto);

    /**
     * Deletes a PremiumSchedule.
     *
     * @param id identifier
     * @return completion signal
     */
    Mono<Void> deletePremiumSchedule(UUID premiumScheduleId);

    /**
     * Loads a PremiumSchedule by id.
     *
     * @param id identifier
     * @return the DTO
     */
    Mono<PremiumScheduleDTO> getPremiumScheduleById(UUID premiumScheduleId);
}
