package com.services.vehicle.infrastructure.persistence.mapper;

import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.valueobject.DocumentNumber;
import com.services.vehicle.domain.valueobject.ValidityPeriod;
import com.services.vehicle.infrastructure.persistence.entity.VehicleDocumentEntity;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Context;

import java.time.LocalDate;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { VehicleDocumentAuditMapper.class }
)
public abstract class VehicleDocumentMapper {

    // -------------------------------------------------------------------------
    // Entity → Domain
    // -------------------------------------------------------------------------

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "documentNumber",
            expression = "java(new DocumentNumber(entity.getDocumentType(), entity.getDocumentNumber()))")
    @Mapping(target = "validityPeriod",
            expression = "java(new ValidityPeriod(entity.getIssueDate(), entity.getExpirationDate()))")
    public abstract VehicleDocument toDomain(VehicleDocumentEntity entity);

    public abstract List<VehicleDocument> toDomainList(List<VehicleDocumentEntity> entities);

    // -------------------------------------------------------------------------
    // Domain → Entity
    // -------------------------------------------------------------------------

    @Mapping(target = "vehicle", expression = "java(vehicleEntity)")
    @Mapping(target = "audits", ignore = true)
    @Mapping(target = "documentNumber", source = "documentNumber.value")
    @Mapping(target = "documentType", source = "documentNumber.type")
    @Mapping(target = "issueDate", source = "validityPeriod.issueDate")
    @Mapping(target = "expirationDate", source = "validityPeriod.expirationDate")
    public abstract VehicleDocumentEntity toEntity(
            VehicleDocument domain,
            @Context VehicleEntity vehicleEntity
    );

    public abstract List<VehicleDocumentEntity> toEntityList(
            List<VehicleDocument> domains,
            @Context VehicleEntity vehicleEntity
    );


}