package com.petsadoption.backend.service;

import com.petsadoption.backend.dto.request.CreatePetRequest;
import com.petsadoption.backend.dto.request.UpdatePetRequest;
import com.petsadoption.backend.dto.response.PetResponse;
import com.petsadoption.backend.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Caso de uso para gestionar mascotas dentro del dominio PETFRIENDLY.
 */
public interface PetService {

    Page<PetResponse> findPets(String city, UUID foundationId, Pet.Species species, Pet.Status status, Pageable pageable);

    PetResponse getPet(UUID id);

    Page<PetResponse> getFoundationPets(UUID foundationId, Pageable pageable);

    PetResponse createPet(CreatePetRequest request);

    PetResponse updatePet(UUID petId, UpdatePetRequest request);

    PetResponse updateStatus(UUID petId, Pet.Status status);

    void deletePet(UUID petId);
}
