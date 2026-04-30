package com.services.vehicle.application.mapper;
import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.domain.model.Vehicle;

import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.domain.valueobject.EngineNumber;
import com.services.vehicle.domain.valueobject.Mileage;

import org.mapstruct.Mapper;



@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface CreateVehicleCommandMapper {

    default Vehicle toDomain(CreateVehicleCommand cmd) {

        return new Vehicle(
                new LicensePlate(cmd.plate()),
                new Vin(cmd.vin()),
                cmd.brand(),
                cmd.line(),
                cmd.modelYear(),
                cmd.displacementCc(),
                cmd.color(),
                cmd.service(),
                cmd.vehicleClass(),
                cmd.bodyType(),
                cmd.fuelType(),
                new EngineNumber(cmd.engineNumber()),
                new Mileage(cmd.initialKm()),
                new Mileage(cmd.currentKm())
        );
    }
}
