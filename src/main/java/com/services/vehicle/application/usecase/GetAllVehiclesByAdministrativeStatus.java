package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.mapper.VehicleResponseMapper;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllVehiclesByAdministrativeStatus implements com.services.vehicle.application.port.in.GetAllVehiclesByAdministrativeStatus {
    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final VehicleResponseMapper vehicleResponseMapper;

    @Override
    public List<VehicleResponse> execute(AdministrativeStatus administrativeStatus) {

        List<Vehicle> vehicles =  vehicleRepositoryPort.findByAdministrativeStatus(administrativeStatus);
        return vehicles.stream()
                .map(vehicleResponseMapper::toResponse)
                .toList();
    }
}
