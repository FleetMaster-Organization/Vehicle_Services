package com.services.vehicle.application.usecase;

import com.services.vehicle.application.dto.RenewVehicleDocumentCommand;
import com.services.vehicle.application.port.in.ActivateVehicleUseCase;
import com.services.vehicle.application.port.in.RenewDocumentUseCase;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.valueobject.DocumentNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RenewDocumentService implements RenewDocumentUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;


    @Override
    public void renewDocument(UUID vehicleId, UUID documentId, RenewVehicleDocumentCommand cmd) {
        Vehicle vehicle = vehicleRepositoryPort.findById(vehicleId);


//        doc.renew(
//                new DocumentNumber(cmd.documentNumber()),
//                cmd.issuedBy(),
//                cmd.issueDate(),
//                cmd.expirationDate()
//        );

        vehicleRepositoryPort.save(vehicle);
    }
}
