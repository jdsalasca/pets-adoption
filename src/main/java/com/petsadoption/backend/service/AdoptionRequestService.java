package com.petsadoption.backend.service;

import com.petsadoption.backend.dto.request.AdoptionRequestCreateRequest;
import com.petsadoption.backend.dto.request.AdoptionRequestStatusUpdateRequest;
import com.petsadoption.backend.dto.response.AdoptionRequestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdoptionRequestService {

    AdoptionRequestResponse create(UUID userId, AdoptionRequestCreateRequest request);

    Page<AdoptionRequestResponse> findMine(UUID userId, Pageable pageable);

    Page<AdoptionRequestResponse> findByFoundation(UUID foundationId, Pageable pageable);

    AdoptionRequestResponse getForUser(UUID requestId, UUID userId);

    void cancel(UUID requestId, UUID userId);

    AdoptionRequestResponse updateStatus(UUID requestId, UUID foundationId, AdoptionRequestStatusUpdateRequest request);
}
