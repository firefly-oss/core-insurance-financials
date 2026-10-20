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
import com.firefly.core.insurance.financials.interfaces.dtos.CreditControlCaseDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for arrears follow-up and lapse notice against a premium schedule.
 */
public interface CreditControlCaseService {

    /**
     * Filters creditControlCases with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of CreditControlCaseDTO
     */
    Mono<PaginationResponse<CreditControlCaseDTO>> filterCreditControlCases(UUID premiumScheduleId, FilterRequest<CreditControlCaseDTO> filterRequest);

    /**
     * Creates a CreditControlCase.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<CreditControlCaseDTO> createCreditControlCase(UUID premiumScheduleId, CreditControlCaseDTO dto);

    /**
     * Updates a CreditControlCase.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<CreditControlCaseDTO> updateCreditControlCase(UUID premiumScheduleId, UUID creditControlCaseId, CreditControlCaseDTO dto);

    /**
     * Deletes a CreditControlCase.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteCreditControlCase(UUID premiumScheduleId, UUID creditControlCaseId);

    /**
     * Loads a CreditControlCase by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<CreditControlCaseDTO> getCreditControlCaseById(UUID premiumScheduleId, UUID creditControlCaseId);
}
