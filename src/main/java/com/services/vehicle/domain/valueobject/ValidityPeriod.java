package com.services.vehicle.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public record ValidityPeriod(
        LocalDate issueDate,
        LocalDate expirationDate
) {

    public ValidityPeriod {

        Objects.requireNonNull(issueDate,
                "Issue date cannot be null");

        Objects.requireNonNull(expirationDate,
                "Expiration date cannot be null");

        if (expirationDate.isBefore(issueDate)) {
            throw new IllegalArgumentException(
                    "Expiration date cannot be before issue date"
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