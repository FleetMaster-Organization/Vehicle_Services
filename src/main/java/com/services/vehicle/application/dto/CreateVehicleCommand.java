package com.services.vehicle.application.dto;

import com.services.vehicle.domain.enums.BodyType;
import com.services.vehicle.domain.enums.FuelType;
import com.services.vehicle.domain.enums.VehicleClass;

public record CreateVehicleCommand(
        String plate,
        String vin,
        String brand,
        String line,
        Integer modelYear,
        Integer displacementCc,
        String color,
        String service,
        VehicleClass vehicleClass,
        BodyType bodyType,
        FuelType fuelType,
        String engineNumber,
        Double initialKm,
        Double currentKm
) {
}