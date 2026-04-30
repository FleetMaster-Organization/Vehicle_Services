package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.CreateVehicleCommand;


import java.util.UUID;

public interface CreateVehicleUseCase {
    UUID create(CreateVehicleCommand command);
}
