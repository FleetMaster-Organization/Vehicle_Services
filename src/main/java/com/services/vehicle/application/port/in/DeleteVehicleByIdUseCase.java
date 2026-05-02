package com.services.vehicle.application.port.in;



import java.util.UUID;

public interface DeleteVehicleByIdUseCase {
    void delete(UUID id);
}
