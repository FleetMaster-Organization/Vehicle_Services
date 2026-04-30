package com.services.vehicle.application.port.out;

import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;

import java.util.List;
import java.util.UUID;

public interface VehicleRepositoryPort {

    Vehicle save(Vehicle vehicle);

    Vehicle findById(UUID id);

    List<Vehicle> findAll();

    Vehicle findByPlate(LicensePlate plate);

    Vehicle findByVin(Vin vin);

    List<Vehicle> findByOperationalStatus(OperationalStatus status);

    List<Vehicle> findByAdministrativeStatus(AdministrativeStatus status);

    boolean existsByPlate(String plate);

    boolean existsByVin(String vin);
}
