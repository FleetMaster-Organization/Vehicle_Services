package com.services.vehicle.application.port.in;

import java.util.UUID;

public interface UpdateDocumentsStatusUseCase {
    void execute(UUID vehicleId, String modifiedBy);
}
