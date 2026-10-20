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
import io.fireflysoftware.core.insurance.financials.interfaces.dtos.IndemnityDisbursementDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for indemnity record pointing at a claim settlement and, once paid, a payment order.
 */
public interface IndemnityDisbursementService {

    /**
     * Filters indemnityDisbursements with the given criteria.
     *
     * @param filterRequest filter criteria
     * @return a page of IndemnityDisbursementDTO
     */
    Mono<PaginationResponse<IndemnityDisbursementDTO>> filterIndemnityDisbursements(FilterRequest<IndemnityDisbursementDTO> filterRequest);

    /**
     * Creates a IndemnityDisbursement.
     *
     * @param dto values to store
     * @return the stored DTO
     */
    Mono<IndemnityDisbursementDTO> createIndemnityDisbursement(IndemnityDisbursementDTO dto);

    /**
     * Updates a IndemnityDisbursement.
     *
     * @param id and dto
     * @return the updated DTO
     */
    Mono<IndemnityDisbursementDTO> updateIndemnityDisbursement(UUID indemnityDisbursementId, IndemnityDisbursementDTO dto);

    /**
     * Deletes a IndemnityDisbursement.
     *
     * @param id identifier
     * @return completion signal
     */
    Mono<Void> deleteIndemnityDisbursement(UUID indemnityDisbursementId);

    /**
     * Loads a IndemnityDisbursement by id.
     *
     * @param id identifier
     * @return the DTO
     */
    Mono<IndemnityDisbursementDTO> getIndemnityDisbursementById(UUID indemnityDisbursementId);
}
