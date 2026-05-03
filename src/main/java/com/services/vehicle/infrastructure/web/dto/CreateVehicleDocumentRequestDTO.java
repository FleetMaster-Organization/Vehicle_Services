package com.services.vehicle.infrastructure.web.dto;

import java.time.LocalDate;

public record CreateVehicleDocumentRequestDTO(
        String documentType,
        String documentNumber,
        String issuedBy,
        LocalDate issueDate,
        LocalDate expirationDate
) {
}
