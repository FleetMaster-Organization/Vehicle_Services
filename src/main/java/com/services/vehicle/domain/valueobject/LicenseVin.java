package com.services.vehicle.domain.valueobject;

import java.util.Objects;

public record LicenseVin(String value) {

    public LicenseVin {
        Objects.requireNonNull(value, "Plate cannot be null");

        String normalized = value
                .trim()
                .toUpperCase();

        if (!normalized.matches("^[0-17]*$")) {
            throw new IllegalArgumentException("invalid licenseVin");
        }

        value = normalized;
    }

    @Override
    public String toString() {
        return value;
    }
}
