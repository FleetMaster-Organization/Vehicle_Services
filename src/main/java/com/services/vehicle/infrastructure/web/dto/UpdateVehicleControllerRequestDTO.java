package com.services.vehicle.infrastructure.web.dto;

import com.services.vehicle.domain.enums.BodyType;
import com.services.vehicle.domain.enums.FuelType;
import com.services.vehicle.domain.enums.ServiceType;

public record UpdateVehicleControllerRequestDTO(
        Integer displacementCc,
        String color,
        ServiceType service,
        BodyType bodyType,
        FuelType fuelType,
        String engineNumber,
        Double currentKm
) {
}
