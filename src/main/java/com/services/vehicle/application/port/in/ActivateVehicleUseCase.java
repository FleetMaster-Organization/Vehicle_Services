package com.services.vehicle.application.port.in;


import java.util.UUID;

public interface ActivateVehicleUseCase {
    void activate(UUID id, String modifiedBy);


}
