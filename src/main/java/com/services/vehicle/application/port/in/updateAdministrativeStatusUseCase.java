package com.services.vehicle.application.port.in;

import com.services.vehicle.domain.enums.AdministrativeStatus;

public interface updateAdministrativeStatusUseCase {
    void updateAdministrativeStatus(AdministrativeStatus administrativeStatus);
}
