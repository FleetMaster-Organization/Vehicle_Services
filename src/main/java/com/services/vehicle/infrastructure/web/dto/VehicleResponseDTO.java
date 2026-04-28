package com.services.vehicle.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleResponseDTO(
        UUID id,
        String message
) {}