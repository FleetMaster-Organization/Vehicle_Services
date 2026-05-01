package com.services.vehicle.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String plate,
        String vin,
        String brand,
        String line,
        Integer modelYear,
        Integer displacementCc,
        String color,
        String service,
        String vehicleClass,
        String bodyType,
        String fuelType,
        String engineNumber,
        Double initialKm,
        Double currentKm,
        LocalDate createdAt,
        LocalDate updatedAt
) {}