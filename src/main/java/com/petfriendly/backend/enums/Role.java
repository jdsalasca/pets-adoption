package com.petfriendly.backend.enums;

/**
 * Enum representing user roles in the PetFriendly platform
 */
public enum Role {
    VISITOR("Visitor"),
    USER("User"),
    FOUNDATION_ADMIN("Foundation Admin"),
    SUPER_ADMIN("Super Admin");

    private final String displayName;

    Role(String displayName) {
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