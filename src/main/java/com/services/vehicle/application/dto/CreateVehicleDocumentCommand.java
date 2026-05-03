package com.services.vehicle.application.dto;



import com.services.vehicle.domain.enums.DocumentType;

import java.time.LocalDate;


public record CreateVehicleDocumentCommand(
        DocumentType documentType,
        String documentNumber,
        String issuedBy,
        LocalDate issueDate,
        LocalDate expirationDate
) {}
