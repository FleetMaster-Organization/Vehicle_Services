package com.services.vehicle.infrastructure.persistence.mapper;

import com.services.vehicle.domain.model.VehicleAudit;
import com.services.vehicle.infrastructure.persistence.entity.VehicleAuditEntity;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class VehicleAuditMapper {

    @Mapping(target = "vehicleId", source = "vehicle.id")
    public abstract VehicleAudit toDomain(VehicleAuditEntity entity);

    public abstract List<VehicleAudit> toDomainList(List<VehicleAuditEntity> entities);

    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "vehicle", expression = "java(vehicleEntity)")
    public abstract VehicleAuditEntity toEntity(
            VehicleAudit domain,
            @Context VehicleEntity vehicleEntity
    );


}