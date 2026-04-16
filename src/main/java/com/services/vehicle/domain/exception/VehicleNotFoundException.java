package com.services.vehicle.domain.exception;

/**
 * Excepción lanzada cuando un vehículo solicitado no existe en el sistema.
 */
public class VehicleNotFoundException extends RuntimeException {

    private final Long vehicleId;
    private final String identifier;

    public VehicleNotFoundException(Long vehicleId) {
        super("Vehículo no encontrado con ID: " + vehicleId);
        this.vehicleId = vehicleId;
        this.identifier = String.valueOf(vehicleId);
    }

    public VehicleNotFoundException(String plate) {
        super("Vehículo no encontrado con placa: " + plate);
        this.vehicleId = null;
        this.identifier = plate;
    }

    public Long getVehicleId() { return vehicleId; }
    public String getIdentifier() { return identifier; }
}
