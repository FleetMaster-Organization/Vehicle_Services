package com.services.vehicle.domain.valueobject;

import jakarta.persistence.Embeddable;
import com.services.vehicle.domain.exception.InvalidDomainDataException;

@Embeddable

public record EngineNumber(
        String value
) {

    public EngineNumber {

        if(value == null || value.isBlank()){
            throw new InvalidDomainDataException("El número de motor no puede estar en blanco.");
        }

        if(value.length() < 6){
            throw new InvalidDomainDataException("El número de motor debe tener al menos 6 caracteres.");
        }
    }
}
