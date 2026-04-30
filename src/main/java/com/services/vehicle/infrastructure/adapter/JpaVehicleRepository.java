package com.services.vehicle.infrastructure.adapter;

import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    boolean existsByPlate(String plate);

    boolean existsByVin(String  vin);

    Optional<VehicleEntity> findByPlate(String plate);

    Optional<VehicleEntity> findByVin(String vin);

    List<VehicleEntity> findByOperationalStatus(OperationalStatus operationalStatus);

    List<VehicleEntity> findByAdministrativeStatus(AdministrativeStatus administrativeStatus);
}