package com.services.vehicle.application.usecase;

import com.services.vehicle.application.port.in.ScrapVehicleByIdUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScrapVehicleByIdService implements ScrapVehicleByIdUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;

    @Override
    public void scrap(UUID id, String modifiedBy) {

        Vehicle vehicle = vehicleRepositoryPort.findById(id);

        vehicle.scrap(modifiedBy);

        vehicleRepositoryPort.save(vehicle);

    }
}
