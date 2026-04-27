package com.services.vehicle.domain.valueobject;

import java.util.Objects;

public record LicensePlate(String value) {

    public LicensePlate {
        Objects.requireNonNull(value, "Plate cannot be null");

        String normalized = value
                .trim()
                .toUpperCase();

        if (!normalized.matches("^[A-Z]{3}[0-9]{3}$")) {
            throw new IllegalArgumentException(
                    "Invalid license plate format"
            );
        }

        value = normalized;
    }

    @Override
    public String toString() {
        return value;
    }
}