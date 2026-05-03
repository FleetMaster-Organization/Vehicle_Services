package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.DocumentResponse;
import com.services.vehicle.application.mapper.DocumentResponseMapper;
import com.services.vehicle.application.port.in.GetDocumentByIdUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetDocumentByIdService implements GetDocumentByIdUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final DocumentResponseMapper documentResponseMapper;

    @Override
    public DocumentResponse execute(UUID vehicleId, UUID documentId) {

        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);

        VehicleDocument document = vehicle.getDocuments().stream()
                .filter(d -> d.getId().equals(documentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Documento no encontrado para este vehículo"));

        return documentResponseMapper.toDocumentResponse(document);
    }
}
