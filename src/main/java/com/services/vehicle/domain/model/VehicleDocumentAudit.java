package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.AuditAction;
import com.services.vehicle.domain.exception.InvalidDomainDataException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
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

    public static VehicleDocumentAudit of(
            UUID documentId,
            AuditAction action,
            String modifiedField,
            String oldValue,
            String newValue,
            String modifiedBy
    ) {
        if (modifiedBy == null || modifiedBy.isBlank()) {
            throw new InvalidDomainDataException("Se requiere modifiedBy para el registro de auditoría.");
        }

        return VehicleDocumentAudit.builder()
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
