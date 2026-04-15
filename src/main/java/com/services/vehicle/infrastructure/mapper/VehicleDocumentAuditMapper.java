package com.services.vehicle.infrastructure.mapper;

import com.services.vehicle.domain.model.VehicleDocumentAudit;
import com.services.vehicle.persistence.entity.VehicleDocumentAuditEntity;
import com.services.vehicle.persistence.entity.VehicleDocumentEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Context;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class VehicleDocumentAuditMapper {

    // -------------------------------------------------------------------------
    // Entidad → Dominio
    // -------------------------------------------------------------------------

    @Mapping(target = "documentId", source = "document.id")
    public abstract VehicleDocumentAudit toDomain(VehicleDocumentAuditEntity entity);

    public abstract List<VehicleDocumentAudit> toDomainList(List<VehicleDocumentAuditEntity> entities);

    // -------------------------------------------------------------------------
    // Dominio → Entidad
    // -------------------------------------------------------------------------

    @Mapping(target = "document", source = "documentEntity")
    public abstract VehicleDocumentAuditEntity toEntity(
            VehicleDocumentAudit domain,
            @Context VehicleDocumentEntity documentEntity
    );

    public abstract List<VehicleDocumentAuditEntity> toEntityList(
            List<VehicleDocumentAudit> domains,
            @Context VehicleDocumentEntity documentEntity
    );
}