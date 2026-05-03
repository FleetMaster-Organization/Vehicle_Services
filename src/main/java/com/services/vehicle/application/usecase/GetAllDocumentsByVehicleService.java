package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.DocumentResponse;
import com.services.vehicle.application.mapper.DocumentResponseMapper;
import com.services.vehicle.application.port.in.GetAllDocumentsByVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllDocumentsByVehicleService implements GetAllDocumentsByVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final DocumentResponseMapper documentResponseMapper;

    @Override
    public List<DocumentResponse> execute(UUID vehicleId) {

        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);

        List<VehicleDocument> documents = vehicle.getDocuments();


        return documents.stream()
                .map(documentResponseMapper::toDocumentResponse)
                .toList();
    }
}
