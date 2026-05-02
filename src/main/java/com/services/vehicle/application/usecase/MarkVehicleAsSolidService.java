package com.services.vehicle.application.usecase;

import com.services.vehicle.application.port.in.MarkVehicleAsSoldUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MarkVehicleAsSolidService implements MarkVehicleAsSoldUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;

    @Override
    public void markVehicleAsSold( UUID id) {

        Vehicle vehicle = vehicleRepositoryPort.findById(id);

        vehicle.markAsSold();

        vehicleRepositoryPort.save(vehicle);


    }
}
