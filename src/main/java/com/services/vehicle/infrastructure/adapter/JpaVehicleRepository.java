package com.services.vehicle.infrastructure.adapter;


import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    boolean existsByPlate(String plate);

    boolean existsByVin(String  vin);

    Optional<VehicleEntity> findByPlate(String plate);

    Optional<VehicleEntity> findByVin(String vin);

}