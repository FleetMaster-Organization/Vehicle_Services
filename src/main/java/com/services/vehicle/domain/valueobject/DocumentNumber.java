package com.services.vehicle.domain.valueobject;

import com.services.vehicle.domain.exception.InvalidDomainDataException;


public record DocumentNumber(String value) {

    public DocumentNumber {
        if (value == null || value.isBlank()) {
            throw new InvalidDomainDataException(
                    "Numero de Documento Invalido"
            );
        }
    }
}