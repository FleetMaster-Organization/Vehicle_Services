package com.services.vehicle.domain.exception;

import java.util.UUID;

/**
 * Excepción lanzada cuando un vehículo solicitado no existe en el sistema.
 */
public class VehicleNotFoundException extends RuntimeException {

    private final UUID vehicleId;
    private final String identifier;

    public VehicleNotFoundException(UUID vehicleId) {
        super("Vehículo no encontrado con ID: " + vehicleId);
        this.vehicleId = vehicleId;
        this.identifier = String.valueOf(vehicleId);
    }

    public VehicleNotFoundException(String plate) {
        super("Vehículo no encontrado con placa: " + plate);
        this.vehicleId = null;
        this.identifier = plate;
    }

    public UUID getVehicleId() { return vehicleId; }
    public String getIdentifier() { return identifier; }
}
