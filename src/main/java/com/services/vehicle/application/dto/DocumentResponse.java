package com.services.vehicle.application.dto;


import java.time.LocalDate;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        UUID vehicleId,
        String documentType,
        String documentNumber,
        String issuedBy,
        LocalDate issueDate,
        LocalDate expirationDate,
        String legalStatus
) {
}
