package com.services.vehicle.infrastructure.web.mapper;

import com.services.vehicle.application.dto.CreateVehicleDocumentCommand;
import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.infrastructure.web.dto.CreateVehicleDocumentRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleDocumentControllerMapper {

    @Mapping(target = "documentType", expression = "java(map(request.documentType()))")
    CreateVehicleDocumentCommand toCommand(CreateVehicleDocumentRequestDTO request);

    default DocumentType map(String value) {
        return DocumentType.valueOf(value.toUpperCase());
    }
}