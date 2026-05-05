package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.RenewVehicleDocumentCommand;
import com.services.vehicle.application.port.in.RenewDocumentUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RenewDocumentService implements RenewDocumentUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;


    @Override
    public void renewDocument(UUID vehicleId, UUID documentId, RenewVehicleDocumentCommand cmd, String modifiedBy) {

        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);

        vehicle.renewDocument(documentId, cmd,modifiedBy);

        vehicleRepositoryPort.save(vehicle);
    }
}
