package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.domain.valueobject.Vin;


public interface GetVehiclesByVinUseCase {
    VehicleResponse execute(Vin vin);
}
