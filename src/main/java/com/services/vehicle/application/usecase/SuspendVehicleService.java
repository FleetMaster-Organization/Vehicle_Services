package com.services.vehicle.application.usecase;

import com.services.vehicle.application.port.in.SuspendVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuspendVehicleService implements SuspendVehicleUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;


    @Override
    public void suspend(UUID id, String reason) {

        Vehicle vehicle = vehicleRepositoryPort.findById(id);

        vehicle.suspend(reason);

        vehicleRepositoryPort.save(vehicle);
    }
}
