package com.services.vehicle.application.port.in;

import java.util.UUID;

public interface ReleaseVehicleUseCase {
    void release(UUID id, String modifiedBy);
}
