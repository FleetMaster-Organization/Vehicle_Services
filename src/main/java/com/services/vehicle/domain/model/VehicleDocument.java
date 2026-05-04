package com.services.vehicle.domain.model;

import com.services.vehicle.application.dto.RenewVehicleDocumentCommand;
import com.services.vehicle.domain.enums.*;
import com.services.vehicle.domain.exception.VehicleDocumentNotFoundException;
import com.services.vehicle.domain.valueobject.DocumentNumber;
import com.services.vehicle.domain.valueobject.ValidityPeriod;
import com.services.vehicle.domain.exception.InvalidDomainDataException;
import com.services.vehicle.domain.exception.InvalidVehicleStateException;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDocument {

    private UUID id;

    private UUID vehicleId;

    private DocumentType documentType;
    private DocumentNumber documentNumber;
    private String issuedBy;

    private ValidityPeriod validityPeriod;

    private LegalStatus legalStatus;

    @Builder.Default
    private List<VehicleDocumentAudit> audits =
            new ArrayList<>();




    public static VehicleDocument create(
            UUID vehicleId,
            DocumentType documentType,
            DocumentNumber documentNumber,
            String issuedBy,
            ValidityPeriod validityPeriod
    ) {
        if (issuedBy == null || issuedBy.isBlank()) {
            throw new InvalidDomainDataException("Se requiere el emisor (issuedBy).");
        }

        validateDocumentNumber(documentType, documentNumber.value());

        return new VehicleDocument(
                null,
                vehicleId,
                documentType,
                documentNumber,
                issuedBy,
                validityPeriod,
                validityPeriod.isExpired(LocalDate.now())
                        ? LegalStatus.EXPIRADO
                        : LegalStatus.VALIDO,
                new ArrayList<>()
        );
    }

    private static void validateDocumentNumber(DocumentType type, String value) {

        switch (type) {
            case SOAT -> {
                if (!value.matches("^[A-Z0-9]{8,20}$")) {
                    throw new InvalidDomainDataException("SOAT inválido");
                }
            }
            case TECNO -> {
                if (!value.matches("^[0-9]{10,15}$")) {
                    throw new InvalidDomainDataException("TECNO inválido");
                }
            }
        }
    }

    public static VehicleDocument rehydrate(
            UUID id,
            UUID vehicleId,
            DocumentType documentType,
            DocumentNumber documentNumber,
            String issuedBy,
            ValidityPeriod validityPeriod,
            LegalStatus legalStatus,
            List<VehicleDocumentAudit> audits
    ) {
        VehicleDocument doc = new VehicleDocument();

        doc.id = id;
        doc.vehicleId = vehicleId;
        doc.documentType = documentType;
        doc.documentNumber = documentNumber;
        doc.issuedBy = issuedBy;
        doc.validityPeriod = validityPeriod;
        doc.legalStatus = legalStatus;
        doc.audits = audits;

        return doc;
    }

    public void renew(
            String newIssuedBy,
            LocalDate newIssueDate,
            LocalDate newExpirationDate,
            String modifiedBy
    ) {

        if (this.legalStatus == LegalStatus.SUSPENDIDO) {
            throw new InvalidVehicleStateException(
                    "No se puede renovar un documento suspendido."
            );
        }

        if (newIssueDate.isAfter(newExpirationDate)) {
            throw new InvalidDomainDataException(
                    "La fecha de emisión no puede ser posterior a la de vencimiento."
            );
        }

        String oldIssuedBy = this.issuedBy;
        String oldIssueDate = this.validityPeriod.issueDate().toString();
        String oldExpirationDate = this.validityPeriod.expirationDate().toString();
        this.issuedBy = newIssuedBy;

        this.validityPeriod = validityPeriod.renew(
                newIssueDate,
                newExpirationDate
        );

        this.legalStatus = LegalStatus.VALIDO;

        registerChange("issuedBy", oldIssuedBy, this.issuedBy, modifiedBy);
        registerChange("issueDate", oldIssueDate, newIssueDate.toString(), modifiedBy);
        registerChange("expirationDate", oldExpirationDate, newExpirationDate.toString(), modifiedBy);
    }

    public void markAsCreated(String createdBy) {
        registerCreationDocument(createdBy);
    }

    private void registerCreationDocument(String createdBy) {
        this.audits.add(VehicleDocumentAudit.of(
                this.id,
                AuditAction.CREATE,
                this.documentType != null ? this.documentType.name() : "document",
                null,
                "CREATED",
                createdBy
        ));
    }


    public boolean isValid(LocalDate today) {
        return legalStatus == LegalStatus.VALIDO
                && validityPeriod.isValid(today);
    }

    public void checkExpiration(LocalDate today, String modifiedBy) {

        if (this.legalStatus == LegalStatus.SUSPENDIDO) {
            return;
        }

        String oldStatus = this.legalStatus.name();

        if (validityPeriod.isExpired(today)) {
            this.legalStatus = LegalStatus.EXPIRADO;
        } else if (validityPeriod.expiresWithin(60, today)) {
            this.legalStatus = LegalStatus.RENOVACION_PENDIENTE;
        } else {
            this.legalStatus = LegalStatus.VALIDO;
        }

        registerChange("legalStatus", oldStatus, this.legalStatus.name(), modifiedBy);
    }

    public boolean isAboutToExpire(
            int days,
            LocalDate today
    ) {
        return validityPeriod.expiresWithin(days, today);
    }


    private void registerChange(
            String field,
            String oldValue,
            String newValue,
            String modifiedBy
    ) {
        if (Objects.equals(oldValue, newValue)) return;

        this.audits.add(VehicleDocumentAudit.of(
                this.id,
                AuditAction.UPDATE,
                field,
                oldValue,
                newValue,
                modifiedBy
        ));
    }


}