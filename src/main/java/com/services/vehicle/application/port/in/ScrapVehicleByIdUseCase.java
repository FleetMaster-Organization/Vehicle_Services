package com.services.vehicle.application.port.in;



import java.util.UUID;

public interface ScrapVehicleByIdUseCase {
    void scrap(UUID id, String modifiedBy);
}
