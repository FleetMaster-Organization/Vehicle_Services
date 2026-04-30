package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.domain.enums.OperationalStatus;

import java.util.List;

public interface GetAllVehiclesByOperationalStatus {
    List<VehicleResponse> execute(OperationalStatus operationalStatus);
}
