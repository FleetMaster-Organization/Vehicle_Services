package com.services.vehicle.application.mapper;

import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.domain.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleResponseMapper {

    @Mapping(target = "plate", expression = "java(vehicle.getPlate().value())")
    @Mapping(target = "vin", expression = "java(vehicle.getVin().value())")
    @Mapping(target = "engineNumber", expression = "java(vehicle.getEngineNumber().value())")
    @Mapping(target = "initialKm", expression = "java(vehicle.getInitialKm().value())")
    @Mapping(target = "currentKm", expression = "java(vehicle.getCurrentKm().value())")
    VehicleResponse toResponse(Vehicle vehicle);
}