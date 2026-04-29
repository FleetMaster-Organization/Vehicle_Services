package com.services.vehicle.infrastructure.adapter;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.exception.VehicleAlreadyExistsException;
import com.services.vehicle.domain.exception.VehicleNotFoundException;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import com.services.vehicle.infrastructure.persistence.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VehicleRepositoryAdapter implements VehicleRepositoryPort {

    private final JpaVehicleRepository jpaVehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public Vehicle save(Vehicle vehicle) {

        if (jpaVehicleRepository.existsByPlate(vehicle.getPlate())) {
            throw new VehicleAlreadyExistsException(vehicle.getPlate());
        }

        if (jpaVehicleRepository.existsByVin(vehicle.getVin())) {
            throw new VehicleAlreadyExistsException(vehicle.getVin());
        }


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
    public Optional<VehicleEntity> findByPlate(String plate) {
        return Optional.empty();
    }

    @Override
    public Optional<VehicleEntity> findByVin(String vin) {
        return Optional.empty();
    }

    @Override
    public List<VehicleEntity> findByOperationalStatus(OperationalStatus status) {
        return List.of();
    }

    @Override
    public List<VehicleEntity> findByAdministrativeStatus(AdministrativeStatus status) {
        return List.of();
    }

    @Override
    public boolean existsByPlate(String plate) {
        return false;
    }

    @Override
    public boolean existsByVin(String vin) {
        return false;
    }
}