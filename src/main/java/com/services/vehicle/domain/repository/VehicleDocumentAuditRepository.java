package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.VehicleDocumentAudit;



import java.util.List;
import java.util.UUID;


public interface VehicleDocumentAuditRepository{


    List<VehicleDocumentAudit> findByDocumentId(UUID documentId);
}
