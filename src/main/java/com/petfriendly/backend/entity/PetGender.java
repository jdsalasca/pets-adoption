package com.petfriendly.backend.entity;

/**
 * Enum representing pet gender in the PetFriendly platform
 */
public enum PetGender {
    MALE("Male"),
    FEMALE("Female"),
    UNKNOWN("Unknown");

    private final String displayName;

    PetGender(String displayName) {
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