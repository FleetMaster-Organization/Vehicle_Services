package com.services.vehicle.application.usecase.vehicle;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.mapper.VehicleResponseMapper;
import com.services.vehicle.application.port.in.vehicle.GetVehicleByIdUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetVehicleByIdService implements GetVehicleByIdUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final VehicleResponseMapper vehicleResponseMapper;

    @Override
    public VehicleResponse execute(UUID id) {
        return vehicleResponseMapper.toResponse(vehicleRepositoryPort.findById(id));
    }
}
