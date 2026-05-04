package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.RenewVehicleDocumentCommand;

import java.util.UUID;

public  interface RenewDocumentUseCase {
    void renewDocument(UUID vehicleId, UUID documentId, RenewVehicleDocumentCommand cmd, String modifiedBy);
}
