package com.services.vehicle.application.usecase;

import com.services.vehicle.application.port.in.SendVehicleToMaintenanceUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SendVehicleToMaintenance implements SendVehicleToMaintenanceUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;

    @Override
    public void sendVehicleToMaintenance(UUID id) {

        Vehicle vehicle = vehicleRepositoryPort.findById(id);

        vehicle.sendToMaintenance();

        vehicleRepositoryPort.save(vehicle);

    }
}
