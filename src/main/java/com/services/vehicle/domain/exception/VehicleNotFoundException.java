package com.services.vehicle.domain.exception;

import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;

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

    public VehicleNotFoundException(Vin vin) {
        super("Vehículo no encontrado con Vin: " + vin);
        this.vehicleId = null;
        this.identifier = vin.value();
    }

    public VehicleNotFoundException(LicensePlate plate) {
        super("Vehículo no encontrado con placa: " + plate);
        this.vehicleId = null;
        this.identifier = plate.value();
    }

    public UUID getVehicleId() { return vehicleId; }
    public String getIdentifier() { return identifier; }
}
