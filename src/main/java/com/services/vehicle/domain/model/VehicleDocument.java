package com.services.vehicle.domain.model;

import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.enums.LegalStatus;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de dominio puro que representa un documento legal asociado a un vehículo.
 * Ejemplos: SOAT, Tecnomecánica, Tarjeta de Propiedad.
 * No contiene anotaciones de persistencia.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDocument {

    private Long id;
    private Long vehicleId;
    private DocumentType documentType;
    private String documentNumber;
    private String issuedBy;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private LegalStatus legalStatus;

    @Builder.Default
    private List<VehicleDocumentAudit> audits = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructor de creación
    // -------------------------------------------------------------------------
    public VehicleDocument(Long vehicleId, DocumentType documentType, String documentNumber,
                           String issuedBy, LocalDate issueDate, LocalDate expirationDate) {
        this.vehicleId = vehicleId;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.issuedBy = issuedBy;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.legalStatus = determineLegalStatus(expirationDate);
        this.audits = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Lógica de negocio
    // -------------------------------------------------------------------------

    /**
     * Renueva el documento con nueva información de expedición y vencimiento.
     */
    public void renew(String newDocumentNumber, String newIssuedBy,
                      LocalDate newIssueDate, LocalDate newExpirationDate) {
        if (newExpirationDate.isBefore(newIssueDate)) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la de expedición.");
        }
        this.documentNumber = newDocumentNumber;
        this.issuedBy = newIssuedBy;
        this.issueDate = newIssueDate;
        this.expirationDate = newExpirationDate;
        this.legalStatus = determineLegalStatus(newExpirationDate);
    }

    /**
     * Verifica si el documento está vigente a la fecha actual.
     */
    public boolean isValid() {
        return this.legalStatus == LegalStatus.VALID && !expirationDate.isBefore(LocalDate.now());
    }

    /**
     * Verifica si el documento vence en los próximos {@code days} días.
     */
    public boolean isAboutToExpire(int days) {
        return !expirationDate.isBefore(LocalDate.now()) &&
                expirationDate.isBefore(LocalDate.now().plusDays(days));
    }

    /**
     * Determina el estado legal basado en la fecha de vencimiento.
     */
    private LegalStatus determineLegalStatus(LocalDate expirationDate) {
        return expirationDate.isBefore(LocalDate.now())
                ? LegalStatus.EXPIRED
                : LegalStatus.VALID;
    }

    /**
     * Marca el documento manualmente como suspendido.
     */
    public void suspend() {
        this.legalStatus = LegalStatus.SUSPENDED;
    }

    /**
     * Marca el documento para renovación pendiente.
     */
    public void markPendingRenewal() {
        this.legalStatus = LegalStatus.PENDING_RENEWAL;
    }
}
