package com.services.vehicle.persistence.entity;

import com.services.vehicle.domain.enums.AuditAction;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que mapea la tabla "vehicle_audits" en la base de datos.
 * Registra el historial de cambios realizados sobre un vehículo.
 * Esta clase pertenece exclusivamente a la capa de infraestructura.
 */
@Entity
@Table(name = "vehicle_audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAuditEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditAction actionType;

    @Column(length = 60)
    private String modifiedField;

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    @Column(nullable = false, length = 80)
    private String modifiedBy;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;
}
