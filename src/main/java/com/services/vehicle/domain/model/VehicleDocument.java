package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.enums.LegalStatus;
import com.services.vehicle.domain.valueobject.DocumentNumber;
import com.services.vehicle.domain.valueobject.ValidityPeriod;
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
            throw new IllegalArgumentException("Issuer (issuedBy) is required");
        }

        return new VehicleDocument(
                UUID.randomUUID(),
                vehicleId,
                documentType,
                documentNumber,
                issuedBy,
                validityPeriod,
                validityPeriod.isExpired(LocalDate.now())
                        ? LegalStatus.EXPIRED
                        : LegalStatus.VALID,
                new ArrayList<>()
        );
    }

    public void renew(
            DocumentNumber newDocumentNumber,
            String newIssuedBy,
            LocalDate newIssueDate,
            LocalDate newExpirationDate
    ) {

        if (this.legalStatus == LegalStatus.SUSPENDED) {
            throw new IllegalStateException(
                    "Cannot renew a suspended document. Lift the suspension first."
            );
        }

        this.documentNumber = newDocumentNumber;
        this.issuedBy = newIssuedBy;

        this.validityPeriod =
                validityPeriod.renew(
                        newIssueDate,
                        newExpirationDate
                );

        this.legalStatus = LegalStatus.VALID;
    }

    public boolean isValid(LocalDate today) {
        return legalStatus == LegalStatus.VALID
                && validityPeriod.isValid(today);
    }

    public void checkExpiration(LocalDate today) {
        if (this.legalStatus == LegalStatus.SUSPENDED) {
            return; // la suspensión tiene precedencia
        }

        if (validityPeriod.isExpired(today)) {
            this.legalStatus = LegalStatus.EXPIRED;
        } else if (validityPeriod.expiresWithin(30, today)) {
            this.legalStatus = LegalStatus.PENDING_RENEWAL;
        } else {
            this.legalStatus = LegalStatus.VALID;
        }
    }

    public boolean isAboutToExpire(
            int days,
            LocalDate today
    ) {
        return validityPeriod.expiresWithin(days, today);
    }

    public void suspend() {
        this.legalStatus = LegalStatus.SUSPENDED;
    }

    public void markPendingRenewal() {
        this.legalStatus = LegalStatus.PENDING_RENEWAL;
    }


}