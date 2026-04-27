package com.services.vehicle.infrastructure.persistence.mapper;

import com.services.vehicle.domain.model.VehicleAudit;
import com.services.vehicle.infrastructure.persistence.entity.VehicleAuditEntity;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Context;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class VehicleAuditMapper {

    // -------------------------------------------------------------------------
    // Entidad → Dominio
    // -------------------------------------------------------------------------

    @Mapping(target = "vehicleId", source = "vehicle.id")
    public abstract VehicleAudit toDomain(VehicleAuditEntity entity);

    public abstract List<VehicleAudit> toDomainList(List<VehicleAuditEntity> entities);

    // -------------------------------------------------------------------------
    // Dominio → Entidad
    // -------------------------------------------------------------------------
    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "vehicle", expression = "java(vehicleEntity)")
    public abstract VehicleAuditEntity toEntity(
            VehicleAudit domain,
            @Context VehicleEntity vehicleEntity
    );

    public abstract List<VehicleAuditEntity> toEntityList(
            List<VehicleAudit> domains,
            @Context VehicleEntity vehicleEntity
    );
}