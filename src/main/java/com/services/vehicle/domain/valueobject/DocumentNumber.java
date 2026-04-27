package com.services.vehicle.domain.valueobject;

import com.services.vehicle.domain.enums.DocumentType;
import jakarta.persistence.Embeddable;
import com.services.vehicle.domain.exception.InvalidDomainDataException;

@Embeddable
public record DocumentNumber(
        DocumentType type,
        String value
) {

    public DocumentNumber {

        if (value == null || value.isBlank()) {
            throw new InvalidDomainDataException(
                    "Invalid document number"
            );
        }

        switch (type) {
            case SOAT -> validateSoat(value);
            case TECNO -> validateTecno(value);
            default -> {}
        }
    }

    private static void validateSoat(String value) {
        // Formato colombiano: alfanumérico, 8-20 caracteres, sin espacios
        if (!value.matches("^[A-Z0-9]{8,20}$")) {
            throw new InvalidDomainDataException(
                    "SOAT number must be alphanumeric, between 8 and 20 characters, uppercase, no spaces"
            );
        }
    }

    private static void validateTecno(String value) {
        // CDA emite códigos numéricos de 10-15 dígitos
        if (!value.matches("^[0-9]{10,15}$")) {
            throw new InvalidDomainDataException(
                    "Tecnomecánica certificate must be numeric, between 10 and 15 digits"
            );
        }
    }
}