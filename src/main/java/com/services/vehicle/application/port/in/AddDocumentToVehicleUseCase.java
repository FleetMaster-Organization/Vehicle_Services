package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.CreateVehicleDocumentCommand;

import java.util.UUID;

public interface AddDocumentToVehicleUseCase {
    UUID addDocument(UUID vehicleId, CreateVehicleDocumentCommand command);
}
