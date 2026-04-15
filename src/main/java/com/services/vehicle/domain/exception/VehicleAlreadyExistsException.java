package com.services.vehicle.domain.exception;

/**
 * Excepción lanzada cuando se intenta registrar un vehículo con una placa o VIN ya existente.
 */
public class VehicleAlreadyExistsException extends RuntimeException {

    public VehicleAlreadyExistsException(String field, String value) {
        super("Ya existe un vehículo con " + field + ": " + value);
    }
}
