package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.UpdateVehicleCommand;

import java.util.UUID;

public interface UpdateVehicleByIdUseCase {
    void update(UpdateVehicleCommand command, UUID id, String modifiedBy);
}
