package com.services.vehicle.domain.valueobject;

import jakarta.persistence.Embeddable;
import com.services.vehicle.domain.exception.InvalidDomainDataException;

@Embeddable

public record EngineNumber(
        String value
) {

    public EngineNumber {

        if(value == null || value.isBlank()){
            throw new InvalidDomainDataException("Engine number cannot be blank");
        }

        if(value.length() < 6){
            throw new InvalidDomainDataException("Engine number must be at least 6 characters long");
        }
    }
}
