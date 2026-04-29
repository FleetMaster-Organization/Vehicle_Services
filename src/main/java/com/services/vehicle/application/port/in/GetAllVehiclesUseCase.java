package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.VehicleResponse;

import java.util.List;


public interface GetAllVehiclesUseCase {
    List<VehicleResponse> execute();
}
