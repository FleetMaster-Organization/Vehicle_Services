package com.services.vehicle.infrastructure.adapter;


import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, UUID> {
}