package com.services.vehicle.infrastructure.persistence.repository;

import com.services.vehicle.domain.model.VehicleDocumentAudit;
import com.services.vehicle.infrastructure.persistence.entity.VehicleDocumentAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

/**
 * Puerto de salida del dominio para la persistencia de auditorías de documentos vehiculares.
 */
public interface VehicleDocumentAuditRepository extends JpaRepository<VehicleDocumentAuditEntity, UUID> {

    /**
     * Retorna todo el historial de auditoría de un documento específico.ss
     */
    List<VehicleDocumentAudit> findByDocumentId(UUID documentId);
}
