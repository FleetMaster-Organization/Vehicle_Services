package com.services.vehicle.domain.repository;

import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.enums.AdministrativeStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida del dominio para la persistencia de vehículos.
 * Esta interfaz es implementada en la capa de infraestructura (adaptador secundario).
 * No debe importar nada de Spring, JPA ni ningún framework externo.
 */

public interface VehicleRepository {

    /**
     * Persiste un vehículo nuevo o actualiza uno existente.
     */
    Vehicle save(Vehicle vehicle);

    /**
     * Busca un vehículo por su ID interno.
     */
    Optional<Vehicle> findById(Long id);

    /**
     * Busca un vehículo por su placa.
     */
    Optional<Vehicle> findByPlate(String plate);

    /**
     * Busca un vehículo por su número VIN.
     */
    Optional<Vehicle> findByVin(String vin);

    /**
     * Retorna todos los vehículos registrados.
     */
    List<Vehicle> findAll();

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

    /**
     * Elimina un vehículo por su ID.
     */
    void deleteById(Long id);
}
