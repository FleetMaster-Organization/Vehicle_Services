package com.services.vehicle.infrastructure.persistence.entity;

import com.services.vehicle.domain.enums.*;

import com.services.vehicle.domain.valueobject.EngineNumber;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.domain.valueobject.Mileage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA que mapea la tabla "vehicles" en la base de datos.
 * Esta clase pertenece exclusivamente a la capa de infraestructura.
 * El dominio no debe conocer ni depender de esta clase.
 */
@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, updatable = false)
    private String plate;

    @Column(unique = true, updatable = false)
    private String vin;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 60)
    private String line;

    @Column(nullable = false, updatable = false)
    private Integer modelYear;

    private Integer displacementCc;

    @Column(length = 30)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ServiceType service;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VehicleClass vehicleClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BodyType bodyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FuelType fuelType;

    private String engineNumber;

    private Double initialKm;

    private Double currentKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OperationalStatus operationalStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdministrativeStatus administrativeStatus;

    @Column(length = 255)
    private String suspensionReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleDocumentEntity> documents = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleAuditEntity> audits = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
