package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.RenewVehicleDocumentCommand;

import java.util.UUID;

public interface ActivateVehicleUseCase {
    void activate(UUID id);


}
