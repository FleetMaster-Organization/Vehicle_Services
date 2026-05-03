package com.services.vehicle.application.mapper;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.dto.CreateVehicleDocumentCommand;
import com.services.vehicle.domain.enums.DocumentType;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.valueobject.*;
import org.mapstruct.Mapper;

import java.util.UUID;


@Mapper(componentModel = "spring")
public interface CreateVehicleDocumentCommandMapper {

    default VehicleDocument toDomain(CreateVehicleDocumentCommand cmd, UUID vehicleId) {

        return VehicleDocument.create(
                vehicleId,
                cmd.documentType(),
                new DocumentNumber(cmd.documentNumber()),
                cmd.issuedBy(),
                new ValidityPeriod(
                        cmd.issueDate(),
                        cmd.expirationDate()
                )
        );
    }
}
