package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.mapper.CreateVehicleCommandMapper;
import com.services.vehicle.application.port.in.CreateVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.exception.VehicleAlreadyExistsException;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateVehicleService implements CreateVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final CreateVehicleCommandMapper mapper;

    @Override
    public UUID create(CreateVehicleCommand cmd, String createdBy) {

        if (vehicleRepositoryPort.existsByPlate(cmd.plate())) {
            throw new VehicleAlreadyExistsException(new LicensePlate(cmd.plate()));
        }

        if (vehicleRepositoryPort.existsByVin(cmd.vin())) {
            throw new VehicleAlreadyExistsException(new Vin(cmd.vin()));
        }

        Vehicle vehicle = mapper.toDomain(cmd);

        vehicle.markAsCreated(createdBy);

        Vehicle saved = vehicleRepositoryPort.save(vehicle);


        return saved.getId();
    }
}