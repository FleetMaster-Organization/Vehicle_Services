package com.services.vehicle.application.port.in;

import java.util.UUID;

public interface AssignVehicleUseCase {
    void assign(UUID id, String modifiedBy);
}
