package com.services.vehicle.application.usecase;

import com.services.vehicle.application.port.in.UpdateDocumentsStatusUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateDocumentStatusService implements UpdateDocumentsStatusUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;

    @Override
    public void execute(UUID vehicleId, String modifiedBy) {

        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);

        vehicle.updateDocumentsStatus(LocalDate.now(),modifiedBy);

        vehicleRepositoryPort.save(vehicle);


    }
}
