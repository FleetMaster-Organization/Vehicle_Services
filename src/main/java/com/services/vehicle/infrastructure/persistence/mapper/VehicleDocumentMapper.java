package com.services.vehicle.infrastructure.persistence.mapper;

import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.valueobject.DocumentNumber;
import com.services.vehicle.domain.valueobject.ValidityPeriod;
import com.services.vehicle.infrastructure.persistence.entity.VehicleDocumentEntity;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {VehicleDocumentAuditMapper.class}
)
public abstract class VehicleDocumentMapper {

    public VehicleDocument toDomain(VehicleDocumentEntity entity) {
        if (entity == null) return null;

        return VehicleDocument.rehydrate(
                entity.getId(),
                entity.getVehicle().getId(),
                entity.getDocumentType(),
                new DocumentNumber(entity.getDocumentNumber()),
                entity.getIssuedBy(),
                new ValidityPeriod(entity.getIssueDate(), entity.getExpirationDate()),
                entity.getLegalStatus(),
                new ArrayList<>()
        );
    }

    public abstract List<VehicleDocument> toDomainList(List<VehicleDocumentEntity> entities);

    @Mapping(target = "vehicle", expression = "java(vehicleEntity)")
    @Mapping(target = "audits", ignore = true)
    @Mapping(target = "documentNumber", source = "documentNumber.value")
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