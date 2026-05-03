package com.services.vehicle.infrastructure.persistence.mapper;


import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.*;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { VehicleDocumentMapper.class, VehicleAuditMapper.class }
)
public abstract class VehicleMapper {

    @Autowired
    protected VehicleDocumentMapper vehicleDocumentMapper;

    @Autowired
    protected VehicleAuditMapper vehicleAuditMapper;

    public Vehicle toDomain(VehicleEntity entity) {

        if (entity == null) return null;

        return Vehicle.rehydrate(
                entity.getId(),
                new LicensePlate(entity.getPlate()),
                new Vin(entity.getVin()),
                entity.getBrand(),
                entity.getLine(),
                entity.getModelYear(),
                entity.getDisplacementCc(),
                entity.getColor(),
                entity.getService(),
                entity.getVehicleClass(),
                entity.getBodyType(),
                entity.getFuelType(),
                new EngineNumber(entity.getEngineNumber()),
                new Mileage(entity.getInitialKm()),
                new Mileage(entity.getCurrentKm()),
                entity.getOperationalStatus(),
                entity.getAdministrativeStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDocuments() != null
                        ? vehicleDocumentMapper.toDomainList(entity.getDocuments())
                        : new ArrayList<>(),
                entity.getAudits() != null
                        ? vehicleAuditMapper.toDomainList(entity.getAudits())
                        : new ArrayList<>()
        );
    }

    public abstract List<Vehicle> toDomainList(List<VehicleEntity> entities);

    @Mapping(target = "audits", ignore = true)
    public abstract VehicleEntity toEntity(Vehicle domain);



    public abstract List<VehicleEntity> toEntityList(List<Vehicle> domains);

    protected LicensePlate map(String value) {
        return value == null ? null : new LicensePlate(value);
    }

    protected String map(LicensePlate plate) {
        return plate == null ? null : plate.value();
    }

    protected Vin mapVin(String value) {
        return value == null ? null : new Vin(value);
    }

    protected String map(Vin vin) {
        return vin == null ? null : vin.value();
    }

    protected EngineNumber mapEngine(String value) {
        return value == null ? null : new EngineNumber(value);
    }

    protected String map(EngineNumber engine) {
        return engine == null ? null : engine.value();
    }

    protected Mileage mapMileage(Double value) {
        return value == null ? null : new Mileage(value);
    }

    protected Double map(Mileage mileage) {
        return mileage == null ? null : mileage.value();
    }

    protected String map(DocumentNumber value) {
        return value == null ? null : value.value();
    }

    protected LocalDate mapIssueDate(ValidityPeriod vp) {
        return vp == null ? null : vp.issueDate();
    }

    protected LocalDate mapExpirationDate(ValidityPeriod vp) {
        return vp == null ? null : vp.expirationDate();
    }


}