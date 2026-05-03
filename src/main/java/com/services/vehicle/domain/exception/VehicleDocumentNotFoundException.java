package com.services.vehicle.domain.exception;

import java.util.UUID;

/**
 * Excepción lanzada cuando un documento vehicular solicitado no existe.
 */
public class VehicleDocumentNotFoundException extends RuntimeException {

    private final UUID documentId;

    public VehicleDocumentNotFoundException(UUID documentId) {
        super("Documento vehicular no encontrado con ID: " + documentId);
        this.documentId = documentId;
    }

    public UUID getDocumentId() { return documentId; }
}
