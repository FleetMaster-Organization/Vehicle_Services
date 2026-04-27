package com.services.vehicle.infrastructure.persistence.repository;

import com.services.vehicle.domain.model.VehicleAudit;
import com.services.vehicle.infrastructure.persistence.entity.VehicleAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

/**
 * Puerto de salida del dominio para la persistencia de auditorías de vehículos.
 */
public interface VehicleAuditRepository extends JpaRepository<VehicleAuditEntity, UUID> {
    /**
     * Retorna todo el historial de auditoría de un vehículo.
     */
    List<VehicleAudit> findByVehicleId(UUID vehicleId);
}
