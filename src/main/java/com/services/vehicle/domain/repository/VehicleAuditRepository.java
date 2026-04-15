package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.VehicleAudit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Puerto de salida del dominio para la persistencia de auditorías de vehículos.
 */
public interface VehicleAuditRepository {

    /**
     * Persiste un registro de auditoría.
     */
    VehicleAudit save(VehicleAudit audit);

    /**
     * Retorna todo el historial de auditoría de un vehículo.
     */
    List<VehicleAudit> findByVehicleId(Long vehicleId);
}
