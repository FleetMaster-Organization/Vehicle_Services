package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.enums.LegalStatus;

import java.util.List;
import java.util.UUID;


public interface VehicleDocumentRepository {


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
