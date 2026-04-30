package com.services.vehicle.application.port.in;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.domain.enums.AdministrativeStatus;

import java.util.List;

public interface GetAllVehiclesByAdministrativeStatus {
    List<VehicleResponse> execute(AdministrativeStatus administrativeStatus);
}
