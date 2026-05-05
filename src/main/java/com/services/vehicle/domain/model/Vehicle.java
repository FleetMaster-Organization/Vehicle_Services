package com.services.vehicle.domain.model;

import com.services.vehicle.application.dto.RenewVehicleDocumentCommand;
import com.services.vehicle.domain.enums.*;
import com.services.vehicle.domain.exception.InvalidDomainDataException;
import com.services.vehicle.domain.exception.InvalidVehicleStateException;
import com.services.vehicle.domain.exception.VehicleDocumentNotFoundException;
import com.services.vehicle.domain.valueobject.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Modelo de dominio puro que representa un vehículo de la flota.
 * No contiene anotaciones de persistencia; esa responsabilidad recae
 * exclusivamente en la capa de infraestructura.
 */
@Getter
@NoArgsConstructor
public class Vehicle {

    private UUID id;
    private LicensePlate plate;
    private Vin vin;
    private String brand;
    private String line;
    private Integer modelYear;
    private Integer displacementCc;
    private String color;
    private ServiceType service;
    private VehicleClass vehicleClass;
    private BodyType bodyType;
    private FuelType fuelType;
    private EngineNumber engineNumber;
    private Mileage initialKm;
    private Mileage currentKm;
    private OperationalStatus operationalStatus;
    private AdministrativeStatus administrativeStatus;
    private String suspensionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<VehicleDocument> documents = new ArrayList<>();
    private List<VehicleAudit> audits = new ArrayList<>();

    // ── Constructor de negocio ────────────────────────────────────────────────

    public Vehicle(LicensePlate plate, Vin vin, String brand, String line,
                   Integer modelYear, Integer displacementCc, String color,
                   ServiceType service, VehicleClass vehicleClass, BodyType bodyType,
                   FuelType fuelType, EngineNumber engineNumber, Mileage initialKm, Mileage currentKm) {

        if (currentKm.lessThan(initialKm)) {
            throw new InvalidDomainDataException("El kilometraje actual no puede ser menor al inicial");
        }

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
        this.operationalStatus = OperationalStatus.ACTIVO;
        this.administrativeStatus = AdministrativeStatus.DISPONIBLE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.documents = new ArrayList<>();
        this.audits = new ArrayList<>();
    }

    public static Vehicle rehydrate(
            UUID id,
            LicensePlate plate,
            Vin vin,
            String brand,
            String line,
            Integer modelYear,
            Integer displacementCc,
            String color,
            ServiceType service,
            VehicleClass vehicleClass,
            BodyType bodyType,
            FuelType fuelType,
            EngineNumber engineNumber,
            Mileage initialKm,
            Mileage currentKm,
            OperationalStatus operationalStatus,
            AdministrativeStatus administrativeStatus,
            String suspensionReason,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<VehicleDocument> documents,
            List<VehicleAudit> audits
    ) {
        Vehicle v = new Vehicle(
                plate, vin, brand, line, modelYear, displacementCc,
                color, service, vehicleClass, bodyType, fuelType,
                engineNumber, initialKm, currentKm
        );

        v.id = id;
        v.operationalStatus = operationalStatus;
        v.administrativeStatus = administrativeStatus;
        v.suspensionReason = suspensionReason;
        v.createdAt = createdAt;
        v.updatedAt = updatedAt;
        v.documents = documents;
        v.audits = audits;

        return v;
    }

    // ── Estado operacional ────────────────────────────────────────────────────

    public void sendToMaintenance(String modifiedBy) {
        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException("Un vehículo dado de baja no puede enviarse a mantenimiento.");
        }
        if (this.operationalStatus == OperationalStatus.EN_MANTENIMIENTO) {
            throw new InvalidVehicleStateException("El vehículo ya está en mantenimiento.");
        }

        updateStatuses(OperationalStatus.EN_MANTENIMIENTO, AdministrativeStatus.RESERVADO, modifiedBy);
    }

    public void assign(LocalDate today, String modifiedBy) {
        if (this.operationalStatus != OperationalStatus.ACTIVO) {
            throw new InvalidVehicleStateException("Solo se pueden asignar vehículos ACTIVOS.");
        }
        if (!isLegallyCompliant(today)) {
            throw new InvalidVehicleStateException(
                    "No se puede asignar el vehículo: uno o más documentos requeridos están vencidos o faltan."
            );
        }

        updateStatuses(OperationalStatus.ASIGNADO, AdministrativeStatus.RESERVADO, modifiedBy);
    }

    public void release(String modifiedBy) {
        if (this.operationalStatus != OperationalStatus.ASIGNADO) {
            throw new InvalidVehicleStateException("El vehículo no está actualmente asignado.");
        }

        updateStatuses(OperationalStatus.ACTIVO, AdministrativeStatus.DISPONIBLE, modifiedBy);
    }

    public void activate(String modifiedBy) {
        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException("Un vehículo dado de baja no puede reactivarse.");
        }
        if (this.administrativeStatus == AdministrativeStatus.VENDIDO) {
            throw new InvalidVehicleStateException("Un vehículo vendido no puede reactivarse.");
        }

        updateStatuses(OperationalStatus.ACTIVO, AdministrativeStatus.DISPONIBLE, modifiedBy);
    }

    public void scrap(String modifiedBy) {
        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException("El vehículo ya ha sido desguazado.");
        }

        updateStatuses(OperationalStatus.DESECHADO, AdministrativeStatus.RETIRADO, modifiedBy);
    }

    public void markAsSold(String modifiedBy) {
        if (this.administrativeStatus != AdministrativeStatus.DISPONIBLE) {
            throw new InvalidVehicleStateException("Solo vehículos DISPONIBLES pueden venderse.");
        }
        if (this.operationalStatus != OperationalStatus.ACTIVO) {
            throw new InvalidVehicleStateException("Solo vehículos ACTIVOS pueden venderse.");
        }

        updateStatuses(OperationalStatus.INACTIVO, AdministrativeStatus.VENDIDO, modifiedBy);
    }

    public void suspend(String reason, String modifiedBy) {
        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException("No se puede suspender un vehículo desechado.");
        }
        if (this.administrativeStatus == AdministrativeStatus.VENDIDO) {
            throw new InvalidVehicleStateException("No se puede suspender un vehículo vendido.");
        }

        String oldReason = this.suspensionReason;
        this.suspensionReason = reason;

        updateStatuses(OperationalStatus.SUSPENDIDO, AdministrativeStatus.RETIRADO, modifiedBy);
        registerChange("suspensionReason", oldReason, reason, modifiedBy);
    }

    public String getSuspensionReason() {
        return this.operationalStatus == OperationalStatus.SUSPENDIDO ? this.suspensionReason : null;
    }

    // ── Documentos ────────────────────────────────────────────────────────────

    public void addDocument(VehicleDocument document, String modifiedBy) {
        DocumentType type = document.getDocumentType();

        boolean exists = this.documents.stream().anyMatch(d -> d.getDocumentType() == type);

        if (!type.isExpirable() && exists) {
            throw new InvalidVehicleStateException("Solo puede existir un documento de tipo: " + type);
        }

        boolean alreadyActive = this.documents.stream()
                .anyMatch(d -> d.getDocumentType() == type && (!type.isExpirable() || d.isValid(LocalDate.now())));

        if (type.isExpirable() && alreadyActive) {
            throw new InvalidVehicleStateException("Ya existe un documento ACTIVO de tipo: " + type);
        }

        document.markAsCreated(modifiedBy);
        this.documents.add(document);
    }

    public void renewDocument(UUID documentId, RenewVehicleDocumentCommand cmd, String modifiedBy) {
        VehicleDocument document = this.documents.stream()
                .filter(d -> d.getId().equals(documentId))
                .findFirst()
                .orElseThrow(() -> new VehicleDocumentNotFoundException(documentId));

        document.renew(cmd.issuedBy(), cmd.issueDate(), cmd.expirationDate(), modifiedBy);
    }

    public void updateDocumentsStatus(LocalDate today, String modifiedBy) {
        this.documents.forEach(doc -> doc.checkExpiration(today, modifiedBy));
    }

    public List<VehicleDocument> documentsAboutToExpire(int days, LocalDate today) {
        return this.documents.stream()
                .filter(d -> d.isAboutToExpire(days, today))
                .toList();
    }

    public boolean hasValidDocument(DocumentType type, LocalDate today) {
        return this.documents.stream()
                .filter(d -> d.getDocumentType() == type)
                .anyMatch(d -> !type.isExpirable() || d.isValid(today));
    }

    public boolean isLegallyCompliant(LocalDate today) {
        return Arrays.stream(DocumentType.values())
                .filter(DocumentType::isRequired)
                .allMatch(type -> hasValidDocument(type, today));
    }

    // ── Campos editables ──────────────────────────────────────────────────────

    public void updateColor(String color, String modifiedBy) {
        String oldColor = this.color;
        this.color = color;
        registerChange("color", oldColor, color, modifiedBy);
    }

    public void updateDisplacement(Integer displacementCc, String modifiedBy) {
        if (displacementCc <= 0) {
            throw new InvalidDomainDataException("La cilindrada debe ser positiva");
        }
        String oldVal = this.displacementCc != null ? this.displacementCc.toString() : null;
        this.displacementCc = displacementCc;
        registerChange("displacementCc", oldVal, displacementCc.toString(), modifiedBy);
    }

    public void updateService(ServiceType service, String modifiedBy) {
        String oldVal = this.service != null ? this.service.name() : null;
        this.service = service;
        registerChange("service", oldVal, service.name(), modifiedBy);
    }

    public void updateBodyType(BodyType bodyType, String modifiedBy) {
        String oldVal = this.bodyType != null ? this.bodyType.name() : null;
        this.bodyType = bodyType;
        registerChange("bodyType", oldVal, bodyType.name(), modifiedBy);
    }

    public void updateFuelType(FuelType fuelType, String modifiedBy) {
        String oldVal = this.fuelType != null ? this.fuelType.name() : null;
        this.fuelType = fuelType;
        registerChange("fuelType", oldVal, fuelType.name(), modifiedBy);
    }

    public void updateEngineNumber(EngineNumber engineNumber, String modifiedBy) {
        String oldVal = this.engineNumber != null ? this.engineNumber.value() : null;
        this.engineNumber = engineNumber;
        registerChange("engineNumber", oldVal, engineNumber.value(), modifiedBy);
    }

    public void updateCurrentKm(Mileage newMileage, String modifiedBy) {
        if (!newMileage.greaterThan(currentKm)) {
            throw new InvalidDomainDataException(
                    "El nuevo kilometraje (%f km) debe ser mayor que el actual (%f km)."
                            .formatted(newMileage.value(), currentKm.value())
            );
        }
        String oldVal = this.currentKm != null ? String.valueOf(this.currentKm.value()) : null;
        this.currentKm = newMileage;
        registerChange("currentKm", oldVal, String.valueOf(newMileage.value()), modifiedBy);
    }

    // ── Auditoría ─────────────────────────────────────────────────────────────

    public void markAsCreated(String createdBy) {
        this.audits.add(VehicleAudit.of(
                this.id,
                AuditAction.CREATE,
                this.plate != null ? this.plate.value() : "vehicle",
                null,
                "CREATED",
                createdBy
        ));
    }

    private void registerChange(String field, String oldValue, String newValue, String modifiedBy) {
        if (Objects.equals(oldValue, newValue)) return;

        this.audits.add(VehicleAudit.of(this.id, AuditAction.UPDATE, field, oldValue, newValue, modifiedBy));
    }

    private void updateStatuses(
            OperationalStatus newOperationalStatus,
            AdministrativeStatus newAdministrativeStatus,
            String modifiedBy
    ) {
        String oldOperationalStatus = this.operationalStatus != null ? this.operationalStatus.name() : null;
        String oldAdministrativeStatus = this.administrativeStatus != null ? this.administrativeStatus.name() : null;

        if (Objects.equals(oldOperationalStatus, newOperationalStatus.name()) &&
                Objects.equals(oldAdministrativeStatus, newAdministrativeStatus.name())) {
            return;
        }

        this.operationalStatus = newOperationalStatus;
        this.administrativeStatus = newAdministrativeStatus;

        registerChange("operationalStatus", oldOperationalStatus, newOperationalStatus.name(), modifiedBy);
        registerChange("administrativeStatus", oldAdministrativeStatus, newAdministrativeStatus.name(), modifiedBy);
    }
}