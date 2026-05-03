package com.services.vehicle.infrastructure.web.mapper;

import com.services.vehicle.application.dto.CreateVehicleDocumentCommand;
import com.services.vehicle.application.dto.DocumentResponse;
import com.services.vehicle.application.dto.RenewVehicleDocumentCommand;
import com.services.vehicle.infrastructure.web.dto.CreateVehicleDocumentRequestDTO;
import com.services.vehicle.infrastructure.web.dto.DocumentControllerResponseDTO;
import com.services.vehicle.infrastructure.web.dto.RenewVehicleControllerRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleDocumentControllerMapper {

    CreateVehicleDocumentCommand toCommand(CreateVehicleDocumentRequestDTO request);

    DocumentControllerResponseDTO toResponse(DocumentResponse response);

    RenewVehicleDocumentCommand toUpdate(RenewVehicleControllerRequestDTO request);


}