package com.services.vehicle.infrastructure.web.mapper;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.infrastructure.web.dto.VehicleControllerRequestDTO;
import com.services.vehicle.infrastructure.web.dto.VehicleControllerResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleControllerMapper {

    CreateVehicleCommand toCommand(VehicleControllerRequestDTO request);

    VehicleControllerResponseDTO toDto(VehicleResponse response);
}
