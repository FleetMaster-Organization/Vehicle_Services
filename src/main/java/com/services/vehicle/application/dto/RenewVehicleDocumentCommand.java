package com.services.vehicle.application.dto;

import java.time.LocalDate;

public record RenewVehicleDocumentCommand(
        String issuedBy,
        String documentNumber,
        LocalDate issueDate,
        LocalDate expirationDate
) {
}
