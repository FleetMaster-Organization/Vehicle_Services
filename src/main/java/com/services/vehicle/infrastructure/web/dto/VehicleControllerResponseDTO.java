package com.services.vehicle.infrastructure.web.dto;

import java.util.UUID;

public record VehicleControllerResponseDTO(
        UUID id,
        String plate,
        String vin,
        String brand,
        String line,
        Integer modelYear,
        Integer currentKm,
        String operationalStatus,
        String administrativeStatus
) {}
