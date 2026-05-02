package com.services.vehicle.infrastructure.web.mapper;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.dto.UpdateVehicleCommand;
import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.infrastructure.web.dto.CreateVehicleControllerRequestDTO;
import com.services.vehicle.infrastructure.web.dto.UpdateVehicleControllerRequestDTO;
import com.services.vehicle.infrastructure.web.dto.VehicleControllerResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleControllerMapper {

    CreateVehicleCommand toCommand(CreateVehicleControllerRequestDTO request);

    UpdateVehicleCommand toUpdate(UpdateVehicleControllerRequestDTO request);

    VehicleControllerResponseDTO toDto(VehicleResponse response);
}
