package com.services.vehicle.application.usecase;

import com.services.vehicle.application.port.in.AssignVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignVehicleService implements AssignVehicleUseCase {

    private final VehicleRepositoryPort  vehicleRepositoryPort;

    @Override
    public void assign(UUID id) {

        Vehicle vehicle = vehicleRepositoryPort.findById(id);

        vehicle.assign(LocalDate.now());

        vehicleRepositoryPort.save(vehicle);
    }
}
