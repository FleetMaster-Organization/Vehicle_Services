package com.services.vehicle.infrastructure.persistence.repository;

import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.enums.LegalStatus;
import com.services.vehicle.infrastructure.persistence.entity.VehicleDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Puerto de salida del dominio para la persistencia de documentos vehiculares.
 * Implementado en la capa de infraestructura.
 */
public interface VehicleDocumentRepository extends JpaRepository<VehicleDocumentEntity, UUID> {


    /**
     * Retorna todos los documentos de un vehículo específico.
     */
    List<VehicleDocument> findByVehicleId(UUID vehicleId);

    /**
     * Retorna los documentos de un vehículo filtrados por tipo.
     */
    List<VehicleDocument> findByVehicleIdAndDocumentType(UUID vehicleId, DocumentType documentType);

    /**
     * Retorna todos los documentos con un estado legal específico.
     */
    List<VehicleDocument> findByLegalStatus(LegalStatus legalStatus);




}
