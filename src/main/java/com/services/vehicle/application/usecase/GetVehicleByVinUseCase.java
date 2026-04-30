package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.mapper.VehicleResponseMapper;
import com.services.vehicle.application.port.in.GetVehiclesByVinUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.valueobject.Vin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetVehicleByVinUseCase implements GetVehiclesByVinUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final VehicleResponseMapper vehicleResponseMapper;

    @Override
    public VehicleResponse execute(Vin vin) {
        return vehicleResponseMapper.toResponse(vehicleRepositoryPort.findByVin(vin));
    }
}
