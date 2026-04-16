package com.services.vehicle.domain.exception;

/**
 * Excepción lanzada cuando un documento vehicular solicitado no existe.
 */
public class VehicleDocumentNotFoundException extends RuntimeException {

    private final Long documentId;

    public VehicleDocumentNotFoundException(Long documentId) {
        super("Documento vehicular no encontrado con ID: " + documentId);
        this.documentId = documentId;
    }

    public Long getDocumentId() { return documentId; }
}
