package com.petfriendly.backend.entity;

/**
 * Enum representing pet adoption status in the PetFriendly platform
 */
public enum PetStatus {
    AVAILABLE("Available"),
    PENDING("Pending Adoption"),
    ADOPTED("Adopted"),
    UNAVAILABLE("Unavailable");

    private final String displayName;

    PetStatus(String displayName) {
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