package com.services.vehicle.domain.valueobject;


import java.util.Objects;
import com.services.vehicle.domain.exception.InvalidDomainDataException;


public record Vin(String value) {

    public Vin {
        Objects.requireNonNull(value, "La placa no puede ser nula.");

        String normalized = value
                .trim()
                .toUpperCase();

        if (!normalized.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
            throw new InvalidDomainDataException("Vin no válido");
        }

        value = normalized;
    }

    @Override
    public String toString() {
        return value;
    }
}
