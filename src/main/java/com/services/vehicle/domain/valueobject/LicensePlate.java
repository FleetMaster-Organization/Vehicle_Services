package com.services.vehicle.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import com.services.vehicle.domain.exception.InvalidDomainDataException;

@Embeddable

public record LicensePlate(String value) {

    public LicensePlate {
        Objects.requireNonNull(value, "Plate cannot be null");

        String normalized = value
                .trim()
                .toUpperCase();

        if (!normalized.matches("^[A-Z]{3}[0-9]{3}$")) {
            throw new InvalidDomainDataException(
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