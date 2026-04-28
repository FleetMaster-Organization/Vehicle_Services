package com.services.vehicle.infrastructure.web.mapper;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.infrastructure.web.dto.VehicleRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleControllerMapper {

    CreateVehicleCommand toCommand(VehicleRequestDTO request);
}
