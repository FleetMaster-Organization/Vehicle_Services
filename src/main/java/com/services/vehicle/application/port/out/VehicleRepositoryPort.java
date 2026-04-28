package com.services.vehicle.application.port.out;

import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleRepositoryPort {

    Vehicle save(Vehicle vehicle);

    Optional<VehicleEntity> findByPlate(String plate);

    Optional<VehicleEntity> findByVin(String vin);

    List<VehicleEntity> findByOperationalStatus(OperationalStatus status);

    List<VehicleEntity> findByAdministrativeStatus(AdministrativeStatus status);

    boolean existsByPlate(String plate);

    boolean existsByVin(String vin);
}
