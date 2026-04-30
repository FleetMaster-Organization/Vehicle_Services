package com.services.vehicle.application.mapper;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.EngineNumber;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Mileage;
import com.services.vehicle.domain.valueobject.Vin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleResponseMapper {

    VehicleResponse toResponse(Vehicle vehicle);

    default String map(LicensePlate value) {
        return value != null ? value.value() : null;
    }

    default String map(Vin value) {
        return value != null ? value.value() : null;
    }

    default String map(EngineNumber value) {
        return value != null ? value.value() : null;
    }

    default Double map(Mileage value) {
        return value != null ? value.value() : null;
    }
}