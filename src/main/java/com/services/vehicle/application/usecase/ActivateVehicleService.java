package com.services.vehicle.application.usecase;

import com.services.vehicle.application.port.in.ActivateVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivateVehicleService implements ActivateVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;

    @Override
    public void activate(UUID id, String modifiedBy) {
        Vehicle vehicle = vehicleRepositoryPort.findById(id);

        vehicle.activate(modifiedBy);

        vehicleRepositoryPort.save(vehicle);
    }
}
