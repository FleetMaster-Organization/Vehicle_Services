package com.services.vehicle.application.mapper;

import com.services.vehicle.application.dto.DocumentResponse;
import com.services.vehicle.domain.model.VehicleDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentResponseMapper {

    @Mapping(target = "documentNumber", source = "documentNumber.value")
    @Mapping(target = "issueDate", source = "validityPeriod.issueDate")
    @Mapping(target = "expirationDate", source = "validityPeriod.expirationDate")
    @Mapping(target = "documentType", source = "documentType")
    @Mapping(target = "legalStatus", source = "legalStatus")
    DocumentResponse toDocumentResponse(VehicleDocument vehicleDocument);
}
