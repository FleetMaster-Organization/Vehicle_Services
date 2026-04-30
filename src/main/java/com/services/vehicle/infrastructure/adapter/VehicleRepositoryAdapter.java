package com.services.vehicle.infrastructure.adapter;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.exception.VehicleAlreadyExistsException;
import com.services.vehicle.domain.exception.VehicleNotFoundException;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import com.services.vehicle.infrastructure.persistence.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VehicleRepositoryAdapter implements VehicleRepositoryPort {

    private final JpaVehicleRepository jpaVehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public Vehicle save(Vehicle vehicle) {

        VehicleEntity entity = vehicleMapper.toEntity(vehicle);

        VehicleEntity saved = jpaVehicleRepository.save(entity);

        return vehicleMapper.toDomain(saved);
    }


    @Override
    public Vehicle findById(UUID id) {
        VehicleEntity vehicle = jpaVehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        return vehicleMapper.toDomain(vehicle);
    }

    @Override
    public List<Vehicle> findAll() {
        List<VehicleEntity> vehicles = jpaVehicleRepository.findAll();
        return vehicleMapper.toDomainList(vehicles) ;
    }

    @Override
    public Vehicle findByPlate(LicensePlate plate) {

        VehicleEntity entity = jpaVehicleRepository.findByPlate(plate.value())
                .orElseThrow(() -> new VehicleNotFoundException(plate));

        return vehicleMapper.toDomain(entity);
    }

    @Override
    public Vehicle findByVin(Vin vin) {
        VehicleEntity vehicle = jpaVehicleRepository.findByVin(vin.value())
                .orElseThrow(() -> new VehicleNotFoundException(vin));

        return vehicleMapper.toDomain(vehicle);
    }

    @Override
    public List<Vehicle> findByOperationalStatus(OperationalStatus status) {
        List<VehicleEntity> vehicles = jpaVehicleRepository.findByOperationalStatus(status);
        return vehicleMapper.toDomainList(vehicles);
    }

    @Override
    public List<Vehicle> findByAdministrativeStatus(AdministrativeStatus status) {
        List<VehicleEntity> vehicles = jpaVehicleRepository.findByAdministrativeStatus(status);
        return vehicleMapper.toDomainList(vehicles);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return jpaVehicleRepository.existsByPlate(plate);
    }

    @Override
    public boolean existsByVin(String vin) {
        return jpaVehicleRepository.existsByVin(vin);
    }
}