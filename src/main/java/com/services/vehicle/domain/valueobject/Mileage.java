package com.services.vehicle.domain.valueobject;

import java.util.Objects;

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