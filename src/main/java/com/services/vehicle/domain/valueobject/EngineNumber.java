package com.services.vehicle.domain.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable

public record EngineNumber(
        String value
) {

    public EngineNumber {

        if(value == null || value.isBlank()){
            throw new IllegalArgumentException();
        }

        if(value.length() < 6){
            throw new IllegalArgumentException();
        }
    }
}
