package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;

import java.util.List;
import java.util.Optional;



public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findByPlate(LicensePlate plate);

    Optional<Vehicle> findByVin(Vin vin);

    List<Vehicle> findByOperationalStatus(OperationalStatus status);

    List<Vehicle> findByAdministrativeStatus(AdministrativeStatus status);

    boolean existsByPlate(LicensePlate plate);

    boolean existsByVin(Vin vin);
}
