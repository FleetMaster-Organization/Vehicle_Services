package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida del dominio para la persistencia de vehículos.
 * Esta interfaz es implementada en la capa de infraestructura (adaptador secundario).
 * No debe importar nada de Spring, JPA ni ningún framework externo.
 */

public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {


    /**
     * Busca un vehículo por su placa.
     */
    Optional<Vehicle> findByPlate(String plate);

    /**
     * Busca un vehículo por su número VIN.
     */
    Optional<Vehicle> findByVin(String vin);


    /**
     * Retorna todos los vehículos con un estado operacional específico.
     */
    List<Vehicle> findByOperationalStatus(OperationalStatus status);

    /**
     * Retorna todos los vehículos con un estado administrativo específico.
     */
    List<Vehicle> findByAdministrativeStatus(AdministrativeStatus status);

    /**
     * Verifica si ya existe un vehículo con la placa dada.
     */
    boolean existsByPlate(String plate);

    /**
     * Verifica si ya existe un vehículo con el VIN dado.
     */
    boolean existsByVin(String vin);

}
