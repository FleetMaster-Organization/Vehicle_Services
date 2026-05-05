package com.services.vehicle.application.port.in;

import java.util.UUID;

public interface SuspendVehicleUseCase {
    void suspend(UUID id, String reason, String modifiedBy);
}
