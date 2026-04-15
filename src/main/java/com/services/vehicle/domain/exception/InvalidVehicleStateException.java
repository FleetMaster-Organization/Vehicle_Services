package com.services.vehicle.domain.exception;

/**
 * Excepción lanzada cuando una transición de estado en el vehículo no es válida
 * según las reglas de negocio definidas en el dominio.
 */
public class InvalidVehicleStateException extends RuntimeException {

    public InvalidVehicleStateException(String message) {
        super(message);
    }

    public InvalidVehicleStateException(String currentState, String targetState) {
        super("Transición de estado inválida: " + currentState + " -> " + targetState);
    }
}
