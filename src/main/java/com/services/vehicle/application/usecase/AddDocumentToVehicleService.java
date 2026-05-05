package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.CreateVehicleDocumentCommand;
import com.services.vehicle.application.mapper.CreateVehicleDocumentCommandMapper;
import com.services.vehicle.application.port.in.AddDocumentToVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.enums.LegalStatus;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddDocumentToVehicleService implements AddDocumentToVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final CreateVehicleDocumentCommandMapper createVehicleDocumentCommandMapper;

    @Override
    public UUID addDocument(UUID vehicleId, CreateVehicleDocumentCommand cmd, String modifiedBy) {

        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);

        VehicleDocument document = createVehicleDocumentCommandMapper.toDomain(cmd, vehicleId);

        if (document.getLegalStatus() == LegalStatus.EXPIRADO) {
            throw new IllegalArgumentException("El documento del vehiculo se encuentra expirado");
        }


        vehicle.addDocument(document, modifiedBy);

        Vehicle savedVehicle = vehicleRepositoryPort.save(vehicle);

        return savedVehicle.getDocuments().stream()
                .filter(d -> d.getDocumentNumber().equals(document.getDocumentNumber()))
                .findFirst()
                .map(VehicleDocument::getId)
                .orElse(null);
    }
}
