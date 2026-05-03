package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.DocumentResponse;
import com.services.vehicle.domain.model.VehicleDocument;

import java.util.List;
import java.util.UUID;

public interface DocumentsAboutToExpireUseCase {
    List<DocumentResponse> execute(UUID vehicleId);
}
