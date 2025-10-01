package com.petfriendly.backend.entity;

/**
 * Enum representing pet size in the PetFriendly platform
 */
public enum PetSize {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large"),
    EXTRA_LARGE("Extra Large");

    private final String displayName;

    PetSize(String displayName) {
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