package com.petsadoption.backend.service.impl;

import com.petsadoption.backend.dto.request.CreatePetRequest;
import com.petsadoption.backend.dto.request.UpdatePetRequest;
import com.petsadoption.backend.dto.response.PetResponse;
import com.petsadoption.backend.entity.Foundation;
import com.petsadoption.backend.entity.Pet;
import com.petsadoption.backend.exception.ResourceNotFoundException;
import com.petsadoption.backend.repository.FoundationRepository;
import com.petsadoption.backend.repository.PetRepository;
import com.petsadoption.backend.service.PetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PetServiceImpl implements PetService {

    private static final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetRepository petRepository;
    private final FoundationRepository foundationRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PetResponse> findPets(String city, UUID foundationId, Pet.Species species, Pet.Status status, Pageable pageable) {
        log.debug("Buscando mascotas - city: {}, foundationId: {}, species: {}, status: {}", city, foundationId, species, status);
        return petRepository.findByFilters(city, foundationId, species, status, pageable)
                .map(PetResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponse getPet(UUID id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota", "id", id));
        return PetResponse.fromEntity(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetResponse> getFoundationPets(UUID foundationId, Pageable pageable) {
        return petRepository.findByFoundationId(foundationId, pageable)
                .map(PetResponse::fromEntity);
    }

    @Override
    public PetResponse createPet(CreatePetRequest request) {
        Foundation foundation = foundationRepository.findById(request.foundationId())
                .orElseThrow(() -> new ResourceNotFoundException("Fundación", "id", request.foundationId()));

        Pet pet = new Pet();
        pet.setFoundation(foundation);
        pet.setName(request.name());
        pet.setSpecies(request.species());
        pet.setBreed(request.breed());
        pet.setSex(request.sex());
        pet.setAgeMonths(request.ageMonths());
        pet.setSize(request.size());
        pet.setTemperament(request.temperament());
        pet.setHealth(request.health());
        pet.setCity(request.city());
        pet.setDescription(request.description());
        pet.setStatus(Pet.Status.AVAILABLE);

        Pet saved = petRepository.save(pet);
        log.info("Mascota creada con id {} para fundación {}", saved.getId(), foundation.getId());
        return PetResponse.fromEntity(saved);
    }

    @Override
    public PetResponse updatePet(UUID petId, UpdatePetRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota", "id", petId));

        request.applyTo(pet);

        Pet updated = petRepository.save(pet);
        log.info("Mascota {} actualizada", petId);
        return PetResponse.fromEntity(updated);
    }

    @Override
    public PetResponse updateStatus(UUID petId, Pet.Status status) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota", "id", petId));
        pet.setStatus(status);
        Pet updated = petRepository.save(pet);
        log.info("Estado de mascota {} actualizado a {}", petId, status);
        return PetResponse.fromEntity(updated);
    }

    @Override
    public void deletePet(UUID petId) {
        if (!petRepository.existsById(petId)) {
            throw new ResourceNotFoundException("Mascota", "id", petId);
        }
        petRepository.deleteById(petId);
        log.info("Mascota {} eliminada", petId);
    }
}