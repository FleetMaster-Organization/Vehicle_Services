package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.DocumentResponse;
import com.services.vehicle.application.mapper.DocumentResponseMapper;
import com.services.vehicle.application.port.in.DocumentsAboutToExpireUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentsAboutToExpireService implements DocumentsAboutToExpireUseCase {

    private final VehicleRepositoryPort  vehicleRepositoryPort;
    private final DocumentResponseMapper documentResponseMapper;

    private static final int EXPIRATION_ALERT_DAYS = 100;

    @Override
    public List<DocumentResponse> execute(UUID vehicleId) {

        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);

        return vehicle.documentsAboutToExpire(EXPIRATION_ALERT_DAYS, LocalDate.now())
                .stream()
                .map(documentResponseMapper::toDocumentResponse)
                .toList();
    }
}
