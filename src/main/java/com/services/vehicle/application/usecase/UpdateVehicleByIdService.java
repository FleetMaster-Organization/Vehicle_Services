package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.UpdateVehicleCommand;
import com.services.vehicle.application.port.in.UpdateVehicleByIdUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.EngineNumber;
import com.services.vehicle.domain.valueobject.Mileage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateVehicleByIdService implements UpdateVehicleByIdUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;

    @Override
    public void update(UpdateVehicleCommand cmd, UUID id, String modifiedBy) {

        if (cmd.displacementCc() == null &&
                cmd.color() == null &&
                cmd.service() == null &&
                cmd.bodyType() == null &&
                cmd.fuelType() == null &&
                cmd.engineNumber() == null &&
                cmd.currentKm() == null) {
            throw new IllegalArgumentException("No hay campos para actualizar");
        }

        Vehicle vehicle = vehicleRepositoryPort.findById(id);

        if (cmd.displacementCc() != null) vehicle.updateDisplacement(cmd.displacementCc(), modifiedBy);
        if (cmd.color() != null)          vehicle.updateColor(cmd.color(), modifiedBy);
        if (cmd.service() != null)        vehicle.updateService(cmd.service(), modifiedBy);
        if (cmd.bodyType() != null)       vehicle.updateBodyType(cmd.bodyType(), modifiedBy);
        if (cmd.fuelType() != null)       vehicle.updateFuelType(cmd.fuelType(), modifiedBy);
        if (cmd.engineNumber() != null)   vehicle.updateEngineNumber(new EngineNumber(cmd.engineNumber()), modifiedBy);
        if (cmd.currentKm() != null)      vehicle.updateCurrentKm(new Mileage(cmd.currentKm()), modifiedBy);

        vehicleRepositoryPort.save(vehicle);
    }
}