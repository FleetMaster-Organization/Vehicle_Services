package com.services.vehicle.infrastructure.web.dto;

import java.util.UUID;

public record CreateVehicleControllerResponseDTO(
        UUID id,
        String message
) {}