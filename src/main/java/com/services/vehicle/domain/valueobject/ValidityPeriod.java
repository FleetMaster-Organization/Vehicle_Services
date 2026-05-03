package com.services.vehicle.domain.valueobject;

import com.services.vehicle.domain.exception.InvalidDomainDataException;

import java.time.LocalDate;
import java.util.Objects;

public record ValidityPeriod(
        LocalDate issueDate,
        LocalDate expirationDate
) {

    public ValidityPeriod {

        Objects.requireNonNull(issueDate,
                "La fecha de emisión no puede ser nula.");

        Objects.requireNonNull(expirationDate,
                "La fecha de caducidad no puede ser nula.");

        if (expirationDate.isBefore(issueDate)) {
            throw new InvalidDomainDataException(
                    "La fecha de vencimiento no puede ser anterior a la fecha de emisión."
            );
        }
    }

    public boolean isExpired(LocalDate today) {
        return expirationDate.isBefore(today);
    }

    public boolean isValid(LocalDate today) {
        return !isExpired(today);
    }

    public boolean expiresWithin(
            int days,
            LocalDate today
    ) {
        return !expirationDate.isBefore(today)
                && expirationDate.isBefore(
                today.plusDays(days)
        );
    }

    public ValidityPeriod renew(
            LocalDate newIssueDate,
            LocalDate newExpirationDate
    ) {
        return new ValidityPeriod(
                newIssueDate,
                newExpirationDate
        );
    }
}