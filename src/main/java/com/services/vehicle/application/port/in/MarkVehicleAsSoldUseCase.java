package com.services.vehicle.application.port.in;

import com.services.vehicle.domain.enums.AdministrativeStatus;

import java.util.UUID;

public interface MarkVehicleAsSoldUseCase {
    void markVehicleAsSold(UUID id);
}
