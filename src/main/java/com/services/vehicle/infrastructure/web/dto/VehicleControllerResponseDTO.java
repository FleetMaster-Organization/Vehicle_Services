package com.services.vehicle.infrastructure.web.dto;

import java.time.LocalDate;
import java.util.UUID;

public record VehicleControllerResponseDTO(
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
        String operationalStatus,
        String administrativeStatus,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
