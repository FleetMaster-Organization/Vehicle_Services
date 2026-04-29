package com.services.vehicle.application.dto;

import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String plate,
        String vin,
        String brand,
        String line,
        Integer modelYear,
        Double initialKm,
        Double currentKm,
        String engineNumber,
        String operationalStatus,
        String administrativeStatus
) {}