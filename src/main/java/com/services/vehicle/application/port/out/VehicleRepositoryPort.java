package com.services.vehicle.application.port.out;

import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepositoryPort {

    Vehicle save(Vehicle vehicle);

    Vehicle findById(UUID id);

    List<Vehicle> findAll();

    Vehicle findByPlate(LicensePlate plate);

    Vehicle findByVin(Vin vin);

    List<VehicleEntity> findByOperationalStatus(OperationalStatus status);

    List<VehicleEntity> findByAdministrativeStatus(AdministrativeStatus status);

    boolean existsByPlate(String plate);

    boolean existsByVin(String vin);
}
