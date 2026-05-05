package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.DocumentResponse;

import java.util.UUID;

public interface GetDocumentByIdUseCase {
    DocumentResponse execute(UUID vehicleId, UUID documentId);
}