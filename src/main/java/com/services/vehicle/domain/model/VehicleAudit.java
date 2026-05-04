package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.AuditAction;
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
public class VehicleAudit {

    private UUID id;
    private UUID vehicleId;
    private AuditAction actionType;
    private String modifiedField;
    private String oldValue;
    private String newValue;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

    public static VehicleAudit of(UUID vehicleId, AuditAction action,
                                  String modifiedField, String oldValue,
                                  String newValue, String modifiedBy) {
        return VehicleAudit.builder()
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
