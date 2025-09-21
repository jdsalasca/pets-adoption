package com.petsadoption.backend.dto.request;

import com.petsadoption.backend.entity.Pet;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Payload para modificaciones parciales de una mascota.
 * Solo se aplican los campos presentes.
 */
public record UpdatePetRequest(
        @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
        String name,

        Pet.Species species,

        @Size(max = 255, message = "La raza no puede exceder 255 caracteres")
        String breed,

        Pet.Sex sex,

        @Min(value = 0, message = "La edad no puede ser negativa")
        @Max(value = 600, message = "La edad no puede exceder 600 meses (50 años)")
        Integer ageMonths,

        Pet.Size size,

        @Size(max = 255, message = "El temperamento no puede exceder 255 caracteres")
        String temperament,

        @Size(max = 2000, message = "La información de salud no puede exceder 2000 caracteres")
        String health,

        @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
        String city,

        @Size(max = 4000, message = "La descripción no puede exceder 4000 caracteres")
        String description,

        Pet.Status status
) {
    /**
     * Aplica los valores presentes al agregado mascota.
     */
    public void applyTo(Pet pet) {
        if (name != null) {
            pet.setName(name);
        }
        if (species != null) {
            pet.setSpecies(species);
        }
        if (breed != null) {
            pet.setBreed(breed);
        }
        if (sex != null) {
            pet.setSex(sex);
        }
        if (ageMonths != null) {
            pet.setAgeMonths(ageMonths);
        }
        if (size != null) {
            pet.setSize(size);
        }
        if (temperament != null) {
            pet.setTemperament(temperament);
        }
        if (health != null) {
            pet.setHealth(health);
        }
        if (city != null) {
            pet.setCity(city);
        }
        if (description != null) {
            pet.setDescription(description);
        }
        if (status != null) {
            pet.setStatus(status);
        }
    }
}
