package com.services.vehicle.infrastructure.web.dto;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentControllerResponseDTO(

        UUID id,
        UUID vehicleId,
        String documentType,
        String documentNumber,
        String issuedBy,
        LocalDate issueDate,
        LocalDate expirationDate,
        String legalStatus
)
{
}
