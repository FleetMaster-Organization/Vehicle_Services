package com.services.vehicle.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public record Mileage(Double value) {

    public Mileage {

        Objects.requireNonNull(value,
                "Mileage cannot be null");

        if (value < 0) {
            throw new IllegalArgumentException(
                    "Mileage cannot be negative"
            );
        }
    }

    public boolean greaterThan(Mileage other){
        return value > other.value;
    }
}