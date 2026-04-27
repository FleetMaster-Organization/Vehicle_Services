package com.services.vehicle.infrastructure.persistence.mapper;

import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { VehicleDocumentMapper.class, VehicleAuditMapper.class }
)
public abstract class VehicleMapper {

    // -------------------------------------------------------------------------
    // Entidad → Dominio
    // -------------------------------------------------------------------------

    public abstract Vehicle toDomain(VehicleEntity entity);

    public abstract List<Vehicle> toDomainList(List<VehicleEntity> entities);

    // -------------------------------------------------------------------------
    // Dominio → Entidad
    // -------------------------------------------------------------------------

    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "audits", ignore = true)
    public abstract VehicleEntity toEntity(Vehicle domain);

    public abstract List<VehicleEntity> toEntityList(List<Vehicle> domains);
}