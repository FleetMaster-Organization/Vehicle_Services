package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.*;

import com.services.vehicle.domain.valueobject.EngineNumber;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.LicenseVin;
import com.services.vehicle.domain.valueobject.Mileage;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio puro que representa un vehículo de la flota.
 * No contiene ninguna anotación de persistencia (JPA/Hibernate).
 * La persistencia es responsabilidad de la capa de infraestructura.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Vehicle {

    private UUID id;
    private LicensePlate plate;
    private LicenseVin vin;
    private String brand;
    private String line;
    private Integer modelYear;
    private Integer displacementCc;
    private String color;
    private String service;
    private VehicleClass vehicleClass;
    private BodyType bodyType;
    private FuelType fuelType;
    private EngineNumber engineNumber;
    private Mileage initialKm;
    private Mileage currentKm;
    private OperationalStatus operationalStatus;
    private AdministrativeStatus administrativeStatus;
    private LocalDateTime createdAt;

    private List<VehicleDocument> documents = new ArrayList<>();

    private List<VehicleAudit> audits = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructor de negocio
    // -------------------------------------------------------------------------
    public Vehicle(LicensePlate plate, LicenseVin vin, String brand, String line,
                   Integer modelYear, Integer displacementCc, String color,
                   String service, VehicleClass vehicleClass, BodyType bodyType,
                   FuelType fuelType, EngineNumber engineNumber, Mileage initialKm, Mileage currentKm) {

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
        this.currentKm = currentKm;
        this.operationalStatus = OperationalStatus.ACTIVE;
        this.administrativeStatus = AdministrativeStatus.AVAILABLE;
        this.createdAt = LocalDateTime.now();
        this.documents = new ArrayList<>();
        this.audits = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Lógica de negocio
    // -------------------------------------------------------------------------

    public void updateCurrentKm(Mileage newMileage) {
        if (!newMileage.greaterThan(currentKm)) {
            throw new IllegalArgumentException(
                    "El nuevo kilometraje (%d km) debe ser mayor que el kilometraje actual (%d km)."
                            .formatted(newMileage.value(), currentKm.value())
            );
        }
        this.currentKm = newMileage;
    }

    public void sendToMaintenance() {
        if (this.operationalStatus == OperationalStatus.SCRAPPED) {
            throw new IllegalStateException(
                    "Un vehículo dado de baja no puede enviarse a mantenimiento."
            );
        }
        if (this.operationalStatus == OperationalStatus.IN_MAINTENANCE) {
            throw new IllegalStateException(
                    "El vehículo ya está en mantenimiento.."
            );
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
        if (this.operationalStatus == OperationalStatus.SCRAPPED) {
            throw new IllegalStateException("El vehículo ya ha sido desguazado.");
        }
        this.operationalStatus = OperationalStatus.SCRAPPED;
        this.administrativeStatus = AdministrativeStatus.SUSPENDED;
    }

    public void assign(LocalDate today) {
        if (this.operationalStatus != OperationalStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Solo se pueden asignar vehículos ACTIVOS."
            );
        }
        if (!isLegallyCompliant(today)) {
            throw new IllegalStateException(
                    "No se puede asignar el vehículo: uno o más documentos requeridos están vencidos o faltan."
            );
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
        boolean alreadyActive = this.documents.stream()
                .anyMatch(d ->
                        d.getDocumentType() == document.getDocumentType()
                                && d.isValid(LocalDate.now())
                );

        if (alreadyActive) {
            throw new IllegalStateException(
                    "El vehículo ya tiene un documento activo del tipo:"
                            + document.getDocumentType()
            );
        }

        this.documents.add(document);
    }

    // ¿Tiene un documento válido de cierto tipo?
    public boolean hasValidDocument(DocumentType type, LocalDate today) {
        return this.documents.stream()
                .anyMatch(d ->
                        d.getDocumentType() == type
                                && d.isValid(today)
                );
    }

    // Documentos próximos a vencer
    public List<VehicleDocument> documentsAboutToExpire(int days, LocalDate today) {
        return this.documents.stream()
                .filter(d -> d.isAboutToExpire(days, today))
                .toList();
    }

    // ¿Está el vehículo en regla? (todos los docs obligatorios vigentes)
    public boolean isLegallyCompliant(LocalDate today) {
        List<DocumentType> required = List.of(
                DocumentType.SOAT,
                DocumentType.TECNO,
                DocumentType.PROPERTY_CARD
        );
        return required.stream()
                .allMatch(type -> hasValidDocument(type, today));
    }


}