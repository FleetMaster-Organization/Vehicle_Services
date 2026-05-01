package com.services.vehicle.application.dto;

import com.services.vehicle.domain.enums.BodyType;
import com.services.vehicle.domain.enums.FuelType;
import com.services.vehicle.domain.enums.ServiceType;

public record UpdateVehicleCommand(
        Integer displacementCc,
        String color,
        ServiceType service,
        BodyType bodyType,
        FuelType fuelType,
        String engineNumber,
        Double initialKm,
        Double currentKm
) {
}