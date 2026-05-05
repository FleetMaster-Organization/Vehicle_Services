package com.services.vehicle.infrastructure.web.dto;

public record SuspendVehicleRequest(
        String suspensionReason,
        String modifiedBy
) {}