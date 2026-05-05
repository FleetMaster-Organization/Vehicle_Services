package com.services.vehicle.application.dto;

import java.time.LocalDate;

public record RenewVehicleDocumentCommand(
        String issuedBy,
        LocalDate issueDate,
        LocalDate expirationDate
) {
}
