package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.VehicleAudit;



import java.util.List;
import java.util.UUID;


public interface VehicleAuditRepository{
    List<VehicleAudit> findByVehicleId(UUID vehicleId);
}
