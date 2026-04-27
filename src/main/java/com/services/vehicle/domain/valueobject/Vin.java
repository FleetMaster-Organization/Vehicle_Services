package com.services.vehicle.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import com.services.vehicle.domain.exception.InvalidDomainDataException;

@Embeddable

public record Vin(String value) {

    public Vin {
        Objects.requireNonNull(value, "Plate cannot be null");

        String normalized = value
                .trim()
                .toUpperCase();

        if (!normalized.matches("^[0-17]*$")) {
            throw new InvalidDomainDataException("invalid licenseVin");
        }

        value = normalized;
    }

    @Override
    public String toString() {
        return value;
    }
}
