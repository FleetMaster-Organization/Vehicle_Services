package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.mapper.CreateVehicleCommandMapper;
import com.services.vehicle.application.port.in.CreateVehicleUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateVehicleService implements CreateVehicleUseCase {

    private final VehicleRepositoryPort repository;
    private final CreateVehicleCommandMapper mapper;

    @Override
    public UUID create(CreateVehicleCommand cmd) {

        Vehicle vehicle = mapper.toDomain(cmd);

        Vehicle saved = repository.save(vehicle);

        return saved.getId();
    }
}