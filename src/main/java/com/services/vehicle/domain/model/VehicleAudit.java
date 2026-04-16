package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.AuditAction;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo de dominio puro que representa un registro de auditoría sobre un vehículo.
 * Registra qué campo fue modificado, por quién, cuándo, y cuál fue el valor anterior y nuevo.
 * No contiene anotaciones de persistencia.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAudit {

    private UUID id;
    private UUID vehicleId;
    private AuditAction actionType;
    private String modifiedField;
    private String oldValue;
    private String newValue;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

    // -------------------------------------------------------------------------
    // Factory method: construye un registro de auditoría
    // -------------------------------------------------------------------------
    public static VehicleAudit of(UUID vehicleId, AuditAction action,
                                  String modifiedField, String oldValue,
                                  String newValue, String modifiedBy) {
        return VehicleAudit.builder()
                .id(UUID.randomUUID())
                .vehicleId(vehicleId)
                .actionType(action)
                .modifiedField(modifiedField)
                .oldValue(oldValue)
                .newValue(newValue)
                .modifiedBy(modifiedBy)
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
