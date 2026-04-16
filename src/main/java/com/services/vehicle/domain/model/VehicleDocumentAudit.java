package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.AuditAction;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo de dominio puro que representa un registro de auditoría sobre un documento vehicular.
 * Traza los cambios realizados a documentos como SOAT o Tecnomecánica.
 * No contiene anotaciones de persistencia.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDocumentAudit {

    private UUID id;
    private UUID documentId;
    private AuditAction actionType;
    private String modifiedField;
    private String oldValue;
    private String newValue;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

    // -------------------------------------------------------------------------
    // Factory method
    // -------------------------------------------------------------------------
    public static VehicleDocumentAudit of(UUID documentId, AuditAction action,
                                          String modifiedField, String oldValue,
                                          String newValue, String modifiedBy) {
        return VehicleDocumentAudit.builder()
                .id(UUID.randomUUID())
                .documentId(documentId)
                .actionType(action)
                .modifiedField(modifiedField)
                .oldValue(oldValue)
                .newValue(newValue)
                .modifiedBy(modifiedBy)
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
