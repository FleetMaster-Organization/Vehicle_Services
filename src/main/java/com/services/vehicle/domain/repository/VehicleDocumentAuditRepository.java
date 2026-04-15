package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.VehicleDocumentAudit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Puerto de salida del dominio para la persistencia de auditorías de documentos vehiculares.
 */
public interface VehicleDocumentAuditRepository {

    /**
     * Persiste un registro de auditoría de documento.
     */
    VehicleDocumentAudit save(VehicleDocumentAudit audit);

    /**
     * Retorna todo el historial de auditoría de un documento específico.ss
     */
    List<VehicleDocumentAudit> findByDocumentId(Long documentId);
}
