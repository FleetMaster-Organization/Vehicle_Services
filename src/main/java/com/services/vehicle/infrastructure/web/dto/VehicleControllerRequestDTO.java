package com.services.vehicle.infrastructure.web.dto;

public record VehicleControllerRequestDTO(
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
        Integer initialKm,
        Integer currentKm
) {}