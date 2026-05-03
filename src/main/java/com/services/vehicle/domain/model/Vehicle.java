package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.*;

import com.services.vehicle.domain.valueobject.EngineNumber;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.domain.valueobject.Mileage;
import com.services.vehicle.domain.exception.InvalidDomainDataException;
import com.services.vehicle.domain.exception.InvalidVehicleStateException;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<VehicleDocument> documents = new ArrayList<>();
    private List<VehicleAudit> audits = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructor de negocio
    // -------------------------------------------------------------------------
    public Vehicle(LicensePlate plate, Vin vin, String brand, String line,
                   Integer modelYear, Integer displacementCc, String color,
                   ServiceType service, VehicleClass vehicleClass, BodyType bodyType,
                   FuelType fuelType, EngineNumber engineNumber, Mileage initialKm, Mileage currentKm) {

        if (currentKm.lessThan(initialKm)) {
            throw new InvalidDomainDataException(
                    "El kilometraje actual no puede ser menor al inicial"
            );
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
        v.createdAt = createdAt;
        v.updatedAt = updatedAt;
        v.documents = documents;
        v.audits = audits;

        return v;
    }

    // -------------------------------------------------------------------------
    // Lógica de negocio
    // -------------------------------------------------------------------------

    public void sendToMaintenance() {
        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException(
                    "Un vehículo dado de baja no puede enviarse a mantenimiento."
            );
        }
        if (this.operationalStatus == OperationalStatus.EN_MANTENIMIENTO) {
            throw new InvalidVehicleStateException(
                    "El vehículo ya está en mantenimiento.."
            );
        }
        this.operationalStatus = OperationalStatus.EN_MANTENIMIENTO;
        this.administrativeStatus = AdministrativeStatus.RESERVADO;
    }

    public void assign(LocalDate today) {
        if (this.operationalStatus != OperationalStatus.ACTIVO) {
            throw new InvalidVehicleStateException(
                    "Solo se pueden asignar vehículos ACTIVOS."
            );
        }
        if (!isLegallyCompliant(today)) {
            throw new InvalidVehicleStateException(
                    "No se puede asignar el vehículo: uno o más documentos requeridos están vencidos o faltan."
            );
        }
        this.administrativeStatus = AdministrativeStatus.ASIGNADO;
    }

    public void release() {
        if (this.administrativeStatus != AdministrativeStatus.ASIGNADO) {
            throw new InvalidVehicleStateException("El vehículo no está actualmente asignado.");
        }
        this.administrativeStatus = AdministrativeStatus.DISPONIBLE;
    }

    public void updateCurrentKm(Mileage newMileage) {
        if (!newMileage.greaterThan(currentKm)) {
            throw new InvalidDomainDataException(
                    "El nuevo kilometraje (%f km) debe ser mayor que el actual (%f km)."
                            .formatted(newMileage.value(), currentKm.value())
            );
        }
        this.currentKm = newMileage;
    }



    public void activate() {
        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException("Un vehículo dado de baja no puede reactivarse.");
        }
        this.operationalStatus = OperationalStatus.ACTIVO;
        this.administrativeStatus = AdministrativeStatus.DISPONIBLE;
    }

    public void scrap() {
        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException("El vehículo ya ha sido desguazado.");
        }
        this.operationalStatus = OperationalStatus.DESECHADO;
        this.administrativeStatus = AdministrativeStatus.SUSPENDIDO;
    }





    public void addDocument(VehicleDocument document) {
        boolean alreadyActive = this.documents.stream()
                .anyMatch(d ->
                        d.getDocumentType() == document.getDocumentType()
                                && d.isValid(LocalDate.now())
                );

        if (alreadyActive) {
            throw new InvalidVehicleStateException(
                    "El vehículo ya tiene un documento activo del tipo:"
                            + document.getDocumentType()
            );
        }

        this.documents.add(document);
    }

    // Documentos próximos a vencer
    public List<VehicleDocument> documentsAboutToExpire(int days, LocalDate today) {
        return this.documents.stream()
                .filter(d -> d.isAboutToExpire(days, today))
                .toList();
    }

    public void markAsSold() {

        if (this.administrativeStatus == AdministrativeStatus.VENDIDO) {
            throw new InvalidVehicleStateException("El vehículo ya está vendido.");
        }

        if (this.operationalStatus == OperationalStatus.DESECHADO) {
            throw new InvalidVehicleStateException("Un vehículo desechado no puede venderse.");
        }

        if (this.administrativeStatus == AdministrativeStatus.SUSPENDIDO) {
            throw new InvalidVehicleStateException("El vehiculo se encuentra actualmente suspendido," +
                    " lo que imposibilita su venta");
        }

        this.administrativeStatus = AdministrativeStatus.VENDIDO;

    }



    public boolean hasValidDocument(DocumentType type, LocalDate today) {
        return this.documents.stream()
                .anyMatch(d ->
                        d.getDocumentType() == type
                                && d.isValid(today)
                );
    }



    // ¿Está el vehículo en regla? (todos los docs obligatorios vigentes)
    public boolean isLegallyCompliant(LocalDate today) {
        List<DocumentType> required = List.of(
                DocumentType.SOAT,
                DocumentType.TECNO,
                DocumentType.TARJETA_PROPIEDAD
        );
        return required.stream()
                .allMatch(type -> hasValidDocument(type, today));
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void updateDisplacement(Integer displacementCc) {
        if (displacementCc <= 0) {
            throw new InvalidDomainDataException("La cilindrada debe ser positiva");
        }
        this.displacementCc = displacementCc;
    }

    public void updateService(ServiceType service) {
        this.service = service;
    }

    public void updateBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public void updateFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public void updateEngineNumber(EngineNumber engineNumber) {
        this.engineNumber = engineNumber;
    }


}