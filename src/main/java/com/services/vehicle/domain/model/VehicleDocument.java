package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.enums.LegalStatus;
import com.services.vehicle.domain.valueobject.DocumentNumber;
import com.services.vehicle.domain.valueobject.ValidityPeriod;
import com.services.vehicle.domain.exception.InvalidDomainDataException;
import com.services.vehicle.domain.exception.InvalidVehicleStateException;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

        return new VehicleDocument(
                UUID.randomUUID(),
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

    public void renew(
            DocumentNumber newDocumentNumber,
            String newIssuedBy,
            LocalDate newIssueDate,
            LocalDate newExpirationDate
    ) {

        if (this.legalStatus == LegalStatus.SUSPENDIDO) {
            throw new InvalidVehicleStateException(
                    "No se puede renovar un documento suspendido. Primero hay que levantar la suspensión."
            );
        }

        this.documentNumber = newDocumentNumber;
        this.issuedBy = newIssuedBy;

        this.validityPeriod =
                validityPeriod.renew(
                        newIssueDate,
                        newExpirationDate
                );

        this.legalStatus = LegalStatus.VALIDO;
    }

    public boolean isValid(LocalDate today) {
        return legalStatus == LegalStatus.VALIDO
                && validityPeriod.isValid(today);
    }

    public void checkExpiration(LocalDate today) {
        if (this.legalStatus == LegalStatus.SUSPENDIDO) {
            return; // la suspensión tiene precedencia
        }

        if (validityPeriod.isExpired(today)) {
            this.legalStatus = LegalStatus.EXPIRADO;
        } else if (validityPeriod.expiresWithin(30, today)) {
            this.legalStatus = LegalStatus.RENOVACION_PENDIENTE;
        } else {
            this.legalStatus = LegalStatus.VALIDO;
        }
    }

    public boolean isAboutToExpire(
            int days,
            LocalDate today
    ) {
        return validityPeriod.expiresWithin(days, today);
    }

    public void suspend() {
        this.legalStatus = LegalStatus.SUSPENDIDO;
    }

    public void markPendingRenewal() {
        this.legalStatus = LegalStatus.RENOVACION_PENDIENTE;
    }


}