package com.teb.practice.model;

import java.util.Set;

public enum RideStatus {
    REQUESTED,
    ASSIGNED,
    STARTED,
    COMPLETED,
    PAYMENT_COMPLETED,
    CANCELLED;

    public Set<RideStatus> allowedNext() {

        return switch (this) {
            case REQUESTED -> Set.of(ASSIGNED, CANCELLED);

            case ASSIGNED -> Set.of(STARTED, CANCELLED);

            case STARTED -> Set.of(COMPLETED);

            case COMPLETED -> Set.of(PAYMENT_COMPLETED);

            case PAYMENT_COMPLETED, CANCELLED -> Set.of();
        };
    }

    public boolean canTransitionTo(RideStatus next) {

        return allowedNext().contains(next);
    }
}
