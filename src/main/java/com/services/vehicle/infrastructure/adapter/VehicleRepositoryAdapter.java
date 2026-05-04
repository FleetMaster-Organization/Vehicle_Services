package com.services.vehicle.infrastructure.adapter;

import com.services.vehicle.application.port.out.VehicleRepositoryPort;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.exception.VehicleNotFoundException;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.domain.model.VehicleAudit;
import com.services.vehicle.domain.model.VehicleDocument;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.infrastructure.persistence.entity.VehicleDocumentEntity;
import com.services.vehicle.infrastructure.persistence.entity.VehicleEntity;
import com.services.vehicle.infrastructure.persistence.mapper.VehicleAuditMapper;
import com.services.vehicle.infrastructure.persistence.mapper.VehicleDocumentAuditMapper;
import com.services.vehicle.infrastructure.persistence.mapper.VehicleDocumentMapper;
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
    private final VehicleDocumentMapper vehicleDocumentMapper;
    private final VehicleAuditMapper vehicleAuditMapper;
    private final VehicleDocumentAuditMapper vehicleDocumentAuditMapper;

    @Override
    public Vehicle save(Vehicle vehicle) {

        VehicleEntity entity;

        if (vehicle.getId() == null) {
            entity = vehicleMapper.toEntity(vehicle);

            if (vehicle.getAudits() != null) {
                entity.getAudits().addAll(
                        vehicleAuditMapper.toEntityList(vehicle.getAudits(), entity)
                );
            }

        } else {
            entity = jpaVehicleRepository.findById(vehicle.getId())
                    .orElseThrow(() -> new VehicleNotFoundException(vehicle.getId()));


            entity.setColor(vehicle.getColor());
            entity.setDisplacementCc(vehicle.getDisplacementCc());
            entity.setCurrentKm(vehicle.getCurrentKm().value());
            entity.setEngineNumber(vehicle.getEngineNumber().value());
            entity.setService(vehicle.getService());
            entity.setBodyType(vehicle.getBodyType());
            entity.setFuelType(vehicle.getFuelType());
            entity.setOperationalStatus(vehicle.getOperationalStatus());
            entity.setAdministrativeStatus(vehicle.getAdministrativeStatus());
            entity.setSuspensionReason(vehicle.getSuspensionReason());

            for (VehicleAudit audit : vehicle.getAudits()) {

                boolean exists = entity.getAudits().stream()
                        .anyMatch(a -> a.getId() != null && a.getId().equals(audit.getId()));

                if (!exists) {
                    entity.getAudits().add(
                            vehicleAuditMapper.toEntity(audit, entity)
                    );
                }
            }

            if (vehicle.getDocuments() != null) {

                for (VehicleDocument docDomain : vehicle.getDocuments()) {

                    VehicleDocumentEntity docEntity = entity.getDocuments().stream()
                            .filter(d -> d.getId().equals(docDomain.getId()))
                            .findFirst()
                            .orElse(null);

                    if (docEntity == null) {
                        docEntity = vehicleDocumentMapper.toEntity(docDomain, entity);
                        entity.getDocuments().add(docEntity);
                    } else {
                        docEntity.setIssuedBy(docDomain.getIssuedBy());
                        docEntity.setLegalStatus(docDomain.getLegalStatus());
                        docEntity.setIssueDate(docDomain.getValidityPeriod().issueDate());
                        docEntity.setExpirationDate(docDomain.getValidityPeriod().expirationDate());
                    }


                    if (docDomain.getAudits() != null) {
                        for (com.services.vehicle.domain.model.VehicleDocumentAudit docAuditDomain : docDomain.getAudits()) {
                            boolean auditExists = docEntity.getAudits().stream()
                                    .anyMatch(a -> a.getId() != null && a.getId().equals(docAuditDomain.getId()));

                            if (!auditExists) {
                                docEntity.getAudits().add(
                                        vehicleDocumentAuditMapper.toEntity(docAuditDomain, docEntity)
                                );
                            }
                        }
                    }
                }
            }

        }

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