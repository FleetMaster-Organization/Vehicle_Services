package com.services.vehicle.application.usecase.vehicle;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.mapper.VehicleResponseMapper;
import com.services.vehicle.application.port.in.vehicle.GetVehiclesByPlateUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.valueobject.LicensePlate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetVehicleByPlateService implements GetVehiclesByPlateUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final VehicleResponseMapper vehicleResponseMapper;

    @Override
    public VehicleResponse execute(LicensePlate plate) {
        return vehicleResponseMapper.toResponse(vehicleRepositoryPort.findByPlate(plate));
    }
}
