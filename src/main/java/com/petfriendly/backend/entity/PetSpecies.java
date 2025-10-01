package com.petfriendly.backend.entity;

/**
 * Enum representing pet species in the PetFriendly platform
 */
public enum PetSpecies {
    DOG("Dog"),
    CAT("Cat"),
    RABBIT("Rabbit"),
    BIRD("Bird"),
    HAMSTER("Hamster"),
    GUINEA_PIG("Guinea Pig"),
    FISH("Fish"),
    REPTILE("Reptile"),
    OTHER("Other");

    private final String displayName;

    PetSpecies(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}