package com.services.vehicle.domain.valueobject;


import java.util.Objects;
import com.services.vehicle.domain.exception.InvalidDomainDataException;

public record Mileage(Double value) {

    public Mileage {

        Objects.requireNonNull(value,
                "El kilometraje no puede ser nulo.");

        if (value < 0) {
            throw new InvalidDomainDataException(
                    "El kilometraje no puede ser negativo."
            );
        }
    }

    public boolean greaterThan(Mileage other){
        return value > other.value;
    }
}