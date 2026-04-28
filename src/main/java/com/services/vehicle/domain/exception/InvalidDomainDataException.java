package com.services.vehicle.domain.exception;

/**
 * Excepción lanzada cuando los datos de entrada para construir un Value Object o Modelo
 * no cumplen con las reglas de validación del dominio (ej. formato de placa inválido).
 */
public class InvalidDomainDataException extends RuntimeException {

    public InvalidDomainDataException(String message) {
        super(message);
    }
}
