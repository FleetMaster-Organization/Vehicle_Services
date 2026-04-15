package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.enums.LegalStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida del dominio para la persistencia de documentos vehiculares.
 * Implementado en la capa de infraestructura.
 */
public interface VehicleDocumentRepository {

    /**
     * Persiste un documento nuevo o actualiza uno existente.
     */
    VehicleDocument save(VehicleDocument document);

    /**
     * Busca un documento por su ID.
     */
    Optional<VehicleDocument> findById(Long id);

    /**
     * Retorna todos los documentos de un vehículo específico.
     */
    List<VehicleDocument> findByVehicleId(Long vehicleId);

    /**
     * Retorna los documentos de un vehículo filtrados por tipo.
     */
    List<VehicleDocument> findByVehicleIdAndDocumentType(Long vehicleId, DocumentType documentType);

    /**
     * Retorna todos los documentos con un estado legal específico.
     */
    List<VehicleDocument> findByLegalStatus(LegalStatus legalStatus);

    /**
     * Retorna documentos cuya fecha de vencimiento es anterior o igual a la fecha dada.
     * Útil para detectar documentos expirados o próximos a vencer.
     */
    List<VehicleDocument> findByExpirationDateBefore(LocalDate date);

    /**
     * Elimina un documento por su ID.
     */
    void deleteById(Long id);
}
