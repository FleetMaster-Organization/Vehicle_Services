package com.services.vehicle.persistence.entity;

import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.enums.LegalStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA que mapea la tabla "vehicle_documents" en la base de datos.
 * Esta clase pertenece exclusivamente a la capa de infraestructura.
 */
@Entity
@Table(name = "vehicle_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDocumentEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentType documentType;

    @Column(nullable = false, length = 60)
    private String documentNumber;

    @Column(length = 100)
    private String issuedBy;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LegalStatus legalStatus;

    @Builder.Default
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleDocumentAuditEntity> audits = new ArrayList<>();
}
