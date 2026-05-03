package com.services.vehicle.application.port.in.vehicle;

import com.services.vehicle.application.dto.CreateVehicleDocumentCommand;

import java.util.UUID;

public interface AddDocumentToVehicleUseCase {
    void addDocument(UUID vehicleId, CreateVehicleDocumentCommand command);
}
