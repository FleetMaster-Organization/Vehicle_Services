package com.services.vehicle.domain.exception;

import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;

/**
 * Excepción lanzada cuando se intenta registrar un vehículo con una placa o VIN ya existente.
 */
public class VehicleAlreadyExistsException extends RuntimeException {

    public VehicleAlreadyExistsException(LicensePlate plate) {
        super("Ya existe un vehículo con placa: " + plate.value());
    }

    public VehicleAlreadyExistsException(Vin vin) {
        super("Ya existe un vehículo con ese codigo vin: " + vin.value());
    }
}
