package com.petfriendly.backend.entity;

/**
 * Enum representing adoption request status in the PetFriendly platform
 */
public enum AdoptionRequestStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");

    private final String displayName;

    AdoptionRequestStatus(String displayName) {
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