package com.services.vehicle.application.port.in.vehicle;



import java.util.UUID;

public interface DeleteVehicleByIdUseCase {
    void delete(UUID id);
}
