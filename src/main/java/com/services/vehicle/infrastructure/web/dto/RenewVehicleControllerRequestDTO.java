package com.services.vehicle.infrastructure.web.dto;

import java.time.LocalDate;

public record RenewVehicleControllerRequestDTO(
        String issuedBy,
        LocalDate issueDate,
        LocalDate expirationDate
) {
}
