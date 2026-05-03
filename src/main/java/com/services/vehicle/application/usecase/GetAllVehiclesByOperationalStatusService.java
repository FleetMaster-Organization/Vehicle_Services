package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.mapper.VehicleResponseMapper;
import com.services.vehicle.application.port.in.GetAllVehiclesByOperationalStatusUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllVehiclesByOperationalStatusService implements GetAllVehiclesByOperationalStatusUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final VehicleResponseMapper vehicleResponseMapper;

    @Override
    public List<VehicleResponse> execute(OperationalStatus operationalStatus) {

        List<Vehicle> vehicles = vehicleRepositoryPort.findByOperationalStatus(operationalStatus);
        return vehicles.stream()
                .map(vehicleResponseMapper::toResponse)
                .toList();
    }
}
