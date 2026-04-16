package com.services.vehicle.infrastructure.mapper;

import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.persistence.entity.VehicleDocumentEntity;
import com.services.vehicle.persistence.entity.VehicleEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Context;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { VehicleDocumentAuditMapper.class }
)
public abstract class VehicleDocumentMapper {

    // -------------------------------------------------------------------------
    // Entidad → Dominio
    // -------------------------------------------------------------------------

    @Mapping(target = "vehicleId", source = "vehicle.id")
    public abstract VehicleDocument toDomain(VehicleDocumentEntity entity);

    public abstract List<VehicleDocument> toDomainList(List<VehicleDocumentEntity> entities);

    // -------------------------------------------------------------------------
    // Dominio → Entidad
    // -------------------------------------------------------------------------

    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "vehicle", expression = "java(vehicleEntity)")
    @Mapping(target = "audits", ignore = true)
    public abstract VehicleDocumentEntity toEntity(
            VehicleDocument domain,
            VehicleEntity vehicleEntity
    );

    public abstract List<VehicleDocumentEntity> toEntityList(
            List<VehicleDocument> domains,
            @Context VehicleEntity vehicleEntity
    );
}