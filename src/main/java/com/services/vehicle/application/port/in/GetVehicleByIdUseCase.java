package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.VehicleResponse;

import java.util.UUID;

public interface GetVehicleByIdUseCase {
    VehicleResponse execute(UUID id);
}