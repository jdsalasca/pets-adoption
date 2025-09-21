package com.petsadoption.backend.service.impl;

import com.petsadoption.backend.dto.request.AdoptionRequestCreateRequest;
import com.petsadoption.backend.dto.request.AdoptionRequestStatusUpdateRequest;
import com.petsadoption.backend.dto.response.AdoptionRequestResponse;
import com.petsadoption.backend.entity.AdoptionRequest;
import com.petsadoption.backend.entity.Pet;
import com.petsadoption.backend.entity.User;
import com.petsadoption.backend.exception.BusinessException;
import com.petsadoption.backend.exception.ResourceNotFoundException;
import com.petsadoption.backend.repository.AdoptionRequestRepository;
import com.petsadoption.backend.repository.PetRepository;
import com.petsadoption.backend.repository.UserRepository;
import com.petsadoption.backend.service.AdoptionRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdoptionRequestServiceImpl implements AdoptionRequestService {

    private static final Logger log = LoggerFactory.getLogger(AdoptionRequestServiceImpl.class);

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Override
    public AdoptionRequestResponse create(UUID userId, AdoptionRequestCreateRequest request) {
        Pet pet = petRepository.findById(request.petId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota", "id", request.petId()));

        if (!pet.isAvailable()) {
            throw new BusinessException("La mascota no está disponible para adopción");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        boolean hasActiveRequest = adoptionRequestRepository.existsByPet_IdAndUser_IdAndStatusIn(
                pet.getId(),
                userId,
                EnumSet.of(AdoptionRequest.Status.SUBMITTED, AdoptionRequest.Status.IN_REVIEW)
        );

        if (hasActiveRequest) {
            throw new BusinessException("Ya tienes una solicitud activa para esta mascota");
        }

        AdoptionRequest adoptionRequest = AdoptionRequest.builder()
                .pet(pet)
                .user(user)
                .status(AdoptionRequest.Status.SUBMITTED)
                .answersJson(request.answersJson())
                .notes(request.notes())
                .createdAt(LocalDateTime.now())
                .build();

        AdoptionRequest saved = adoptionRequestRepository.save(adoptionRequest);
        log.info("Solicitud de adopción {} creada por usuario {}", saved.getId(), userId);
        return AdoptionRequestResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequestResponse> findMine(UUID userId, Pageable pageable) {
        return adoptionRequestRepository.findByUser_Id(userId, pageable)
                .map(AdoptionRequestResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequestResponse> findByFoundation(UUID foundationId, Pageable pageable) {
        return adoptionRequestRepository.findByPet_Foundation_Id(foundationId, pageable)
                .map(AdoptionRequestResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public AdoptionRequestResponse getForUser(UUID requestId, UUID userId) {
        AdoptionRequest request = adoptionRequestRepository.findByIdAndUser_Id(requestId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de adopción", "id", requestId));
        return AdoptionRequestResponse.fromEntity(request);
    }

    @Override
    public void cancel(UUID requestId, UUID userId) {
        AdoptionRequest request = adoptionRequestRepository.findByIdAndUser_Id(requestId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de adopción", "id", requestId));

        if (request.getStatus() != AdoptionRequest.Status.SUBMITTED &&
                request.getStatus() != AdoptionRequest.Status.IN_REVIEW) {
            throw new BusinessException("Solo puedes cancelar solicitudes en estado enviado o en revisión");
        }

        request.setStatus(AdoptionRequest.Status.CANCELLED);
        request.setUpdatedAt(LocalDateTime.now());
        adoptionRequestRepository.save(request);
        log.info("Solicitud de adopción {} cancelada por usuario {}", requestId, userId);
    }

    @Override
    public AdoptionRequestResponse updateStatus(UUID requestId, UUID foundationId, AdoptionRequestStatusUpdateRequest updateRequest) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de adopción", "id", requestId));

        Pet pet = adoptionRequest.getPet();
        if (pet == null || pet.getFoundation() == null || !foundationId.equals(pet.getFoundation().getId())) {
            throw new BusinessException("La solicitud no pertenece a la fundación indicada");
        }

        adoptionRequest.setStatus(updateRequest.status());
        adoptionRequest.setNotes(updateRequest.notes());
        adoptionRequest.setReviewedAt(LocalDateTime.now());
        adoptionRequest.setUpdatedAt(LocalDateTime.now());
        adoptionRequest.setReviewedBy(foundationId);

        if (updateRequest.status() == AdoptionRequest.Status.APPROVED) {
            pet.setStatus(Pet.Status.RESERVED);
            petRepository.save(pet);
        } else if (updateRequest.status() == AdoptionRequest.Status.REJECTED ||
                   updateRequest.status() == AdoptionRequest.Status.CANCELLED) {
            pet.setStatus(Pet.Status.AVAILABLE);
            petRepository.save(pet);
        }

        AdoptionRequest saved = adoptionRequestRepository.save(adoptionRequest);
        log.info("Solicitud {} actualizada a {} por fundación {}", requestId, updateRequest.status(), foundationId);
        return AdoptionRequestResponse.fromEntity(saved);
    }
}
