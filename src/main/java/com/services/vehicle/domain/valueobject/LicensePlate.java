package com.services.vehicle.domain.valueobject;


import java.util.Objects;
import com.services.vehicle.domain.exception.InvalidDomainDataException;

public record LicensePlate(String value) {

    public LicensePlate {
        Objects.requireNonNull(value, "La placa no puede ser nula");

        String normalized = value
                .trim()
                .toUpperCase();

        if (!normalized.matches("^[A-Z]{3}[0-9]{3}$")) {
            throw new InvalidDomainDataException(
                    "Formato de placa  invalido"
            );
        }

        value = normalized;
    }

    @Override
    public String toString() {
        return value;
    }
}