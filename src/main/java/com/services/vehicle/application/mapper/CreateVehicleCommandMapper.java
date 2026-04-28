package com.services.vehicle.application.mapper;
import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.domain.model.Vehicle;

import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.domain.valueobject.EngineNumber;
import com.services.vehicle.domain.valueobject.Mileage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = {
                LicensePlate.class,
                Vin.class,
                EngineNumber.class,
                Mileage.class
        }
)
public interface CreateVehicleCommandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt",
            expression = "java(java.time.LocalDateTime.now())")

    @Mapping(target = "operationalStatus", constant = "ACTIVE")
    @Mapping(target = "administrativeStatus", constant = "AVAILABLE")

    @Mapping(target = "documents",
            expression = "java(new java.util.ArrayList<>())")

    @Mapping(target = "audits",
            expression = "java(new java.util.ArrayList<>())")

    @Mapping(target = "plate",
            expression = "java(new LicensePlate(cmd.plate()))")

    @Mapping(target = "vin",
            expression = "java(new Vin(cmd.vin()))")

    @Mapping(target = "engineNumber",
            expression = "java(new EngineNumber(cmd.engineNumber()))")

    @Mapping(target = "initialKm",
            expression = "java(new Mileage(cmd.initialKm()))")

    @Mapping(target = "currentKm",
            expression = "java(new Mileage(cmd.currentKm()))")

    Vehicle toDomain(CreateVehicleCommand cmd);
}
