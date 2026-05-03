package com.services.vehicle.application.usecase.vehicle;

import com.services.vehicle.application.dto.CreateVehicleDocumentCommand;
import com.services.vehicle.application.mapper.CreateVehicleDocumentCommandMapper;
import com.services.vehicle.application.port.in.vehicle.AddDocumentToVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.valueobject.DocumentNumber;
import com.services.vehicle.domain.valueobject.ValidityPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddDocumentToVehicleService implements AddDocumentToVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final CreateVehicleDocumentCommandMapper createVehicleDocumentCommandMapper;

    @Override
    public void addDocument(UUID vehicleId, CreateVehicleDocumentCommand cmd) {

        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);

        VehicleDocument document = createVehicleDocumentCommandMapper.toDomain(cmd, vehicleId);

        vehicle.addDocument(document);

        vehicleRepositoryPort.save(vehicle);
    }
}
