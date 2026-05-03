package com.services.vehicle.application.port.in.vehicle;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.domain.valueobject.LicensePlate;

public interface GetVehiclesByPlateUseCase {
    VehicleResponse execute(LicensePlate plate);

}
