package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.BodyType;
import com.services.vehicle.domain.enums.FuelType;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.enums.VehicleClass;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de dominio puro que representa un vehículo de la flota.
 * No contiene ninguna anotación de persistencia (JPA/Hibernate).
 * La persistencia es responsabilidad de la capa de infraestructura.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    private Long id;
    private String plate;
    private String vin;
    private String brand;
    private String line;
    private Integer modelYear;
    private Integer displacementCc;
    private String color;
    private String service;
    private VehicleClass vehicleClass;
    private BodyType bodyType;
    private FuelType fuelType;
    private String engineNumber;
    private Double initialKm;
    private Double currentKm;
    private OperationalStatus operationalStatus;
    private AdministrativeStatus administrativeStatus;
    private LocalDateTime createdAt;

    @Builder.Default
    private List<VehicleDocument> documents = new ArrayList<>();

    @Builder.Default
    private List<VehicleAudit> audits = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Const ructor de negocio
    // -------------------------------------------------------------------------
    public Vehicle(String plate, String vin, String brand, String line,
                   Integer modelYear, Integer displacementCc, String color,
                   String service, VehicleClass vehicleClass, BodyType bodyType,
                   FuelType fuelType, String engineNumber, Double initialKm) {

        this.plate = plate;
        this.vin = vin;
        this.brand = brand;
        this.line = line;
        this.modelYear = modelYear;
        this.displacementCc = displacementCc;
        this.color = color;
        this.service = service;
        this.vehicleClass = vehicleClass;
        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.engineNumber = engineNumber;
        this.initialKm = initialKm;
        this.currentKm = initialKm;
        this.operationalStatus = OperationalStatus.ACTIVE;
        this.administrativeStatus = AdministrativeStatus.AVAILABLE;
        this.createdAt = LocalDateTime.now();
        this.documents = new ArrayList<>();
        this.audits = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Lógica de negocio
    // -------------------------------------------------------------------------

    public void updateCurrentKm(Double newKm) {
        if (newKm == null || newKm < this.currentKm) {
            throw new IllegalArgumentException(
                    "El nuevo kilometraje (" + newKm + ") no puede ser menor al actual (" + this.currentKm + ")."
            );
        }
        this.currentKm = newKm;
    }

    public void sendToMaintenance() {
        if (this.operationalStatus == OperationalStatus.SCRAPPED) {
            throw new IllegalStateException("Un vehículo dado de baja no puede enviarse a mantenimiento.");
        }
        this.operationalStatus = OperationalStatus.IN_MAINTENANCE;
        this.administrativeStatus = AdministrativeStatus.RESERVED;
    }

    public void activate() {
        if (this.operationalStatus == OperationalStatus.SCRAPPED) {
            throw new IllegalStateException("Un vehículo dado de baja no puede reactivarse.");
        }
        this.operationalStatus = OperationalStatus.ACTIVE;
        this.administrativeStatus = AdministrativeStatus.AVAILABLE;
    }

    public void scrap() {
        this.operationalStatus = OperationalStatus.SCRAPPED;
        this.administrativeStatus = AdministrativeStatus.SUSPENDED;
    }

    public void assign() {
        if (this.operationalStatus != OperationalStatus.ACTIVE) {
            throw new IllegalStateException("Solo se puede asignar un vehículo en estado ACTIVE.");
        }
        this.administrativeStatus = AdministrativeStatus.ASSIGNED;
    }

    public void release() {
        if (this.administrativeStatus != AdministrativeStatus.ASSIGNED) {
            throw new IllegalStateException("El vehículo no está actualmente asignado.");
        }
        this.administrativeStatus = AdministrativeStatus.AVAILABLE;
    }

    public void addDocument(VehicleDocument document) {
        this.documents.add(document);
    }
}