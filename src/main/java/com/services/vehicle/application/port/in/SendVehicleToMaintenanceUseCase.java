package com.services.vehicle.application.port.in;

import java.util.UUID;

public interface SendVehicleToMaintenanceUseCase {
    void sendVehicleToMaintenance(UUID id, String modifiedBy);
}
