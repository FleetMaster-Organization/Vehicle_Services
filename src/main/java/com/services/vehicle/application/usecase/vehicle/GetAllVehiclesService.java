package com.services.vehicle.application.usecase.vehicle;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.mapper.VehicleResponseMapper;
import com.services.vehicle.application.port.in.vehicle.GetAllVehiclesUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllVehiclesService implements GetAllVehiclesUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final VehicleResponseMapper vehicleResponseMapper;


    @Override
    public List<VehicleResponse> execute() {

        List<Vehicle> vehicles = vehicleRepositoryPort.findAll();

        return vehicles.stream()
                .map(vehicleResponseMapper::toResponse)
                .toList();
    }
}
