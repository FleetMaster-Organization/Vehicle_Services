package com.services.vehicle.infrastructure.web.controller;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.dto.UpdateVehicleCommand;
import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.port.in.*;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.infrastructure.web.dto.CreateVehicleControllerResponseDTO;
import com.services.vehicle.infrastructure.web.dto.CreateVehicleControllerRequestDTO;
import com.services.vehicle.infrastructure.web.dto.UpdateVehicleControllerRequestDTO;
import com.services.vehicle.infrastructure.web.dto.VehicleControllerResponseDTO;
import com.services.vehicle.infrastructure.web.mapper.VehicleControllerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleControllerMapper vehicleControllerMapper;
    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetVehicleByIdUseCase getVehicleByIdUseCase;
    private final GetVehiclesByPlateUseCase getVehicleByPlateUseCase;
    private final GetVehiclesByVinUseCase  getVehiclesByVinUseCase;
    private final GetAllVehiclesUseCase getAllVehiclesUseCase;
    private final GetAllVehiclesByAdministrativeStatusUseCase getVehiclesByAdministrativeStatus;
    private final GetAllVehiclesByOperationalStatusUseCase getVehiclesByOperationalStatus;
    private final UpdateVehicleByIdUseCase updateVehicleByIdUseCase;
    private final DeleteVehicleByIdUseCase deleteVehicleByIdUseCase;
    private final MarkVehicleAsSoldUseCase markVehicleAsSoldUseCase;

    @PostMapping
    public ResponseEntity<CreateVehicleControllerResponseDTO> createVehicle(
            @RequestBody CreateVehicleControllerRequestDTO request) {

        CreateVehicleCommand command = vehicleControllerMapper.toCommand(request);

        UUID id = createVehicleUseCase.create(command);

        CreateVehicleControllerResponseDTO response = new CreateVehicleControllerResponseDTO(
                id,
                "Vehículo creado correctamente"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleById(@PathVariable UUID id) {

        VehicleResponse response = getVehicleByIdUseCase.execute(id);

        return ResponseEntity.ok(
                vehicleControllerMapper.toDto(response)
        );
    }

    @GetMapping
    public ResponseEntity<List<VehicleControllerResponseDTO>> getAllVehicles() {

        List<VehicleResponse> vehicles = getAllVehiclesUseCase.execute();

        List<VehicleControllerResponseDTO> response = vehicles.stream()
                .map(vehicleControllerMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{plate}/plate")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleByPlate(@PathVariable String plate){

        LicensePlate licensePlate = new LicensePlate(plate);

        VehicleResponse response = getVehicleByPlateUseCase.execute(licensePlate);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }

    @GetMapping("{vin}/vin/")
    public ResponseEntity<VehicleControllerResponseDTO> getVehiclesByVin(@PathVariable String vin){

        Vin vinNumber = new Vin(vin);

        VehicleResponse response = getVehiclesByVinUseCase.execute(vinNumber);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }

    @GetMapping("/{operationalStatus}/operational-status")
    public ResponseEntity<List<VehicleControllerResponseDTO>> getVehiclesByOperationalStatus(@PathVariable String operationalStatus){

        OperationalStatus operationalStatusEnum = OperationalStatus.valueOf(operationalStatus.toUpperCase());

        List<VehicleResponse> vehicles = getVehiclesByOperationalStatus.execute(operationalStatusEnum);

        List<VehicleControllerResponseDTO> response = vehicles.stream()
                .map(vehicleControllerMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{administrativeStatus}/administrative-status")
    public ResponseEntity<List<VehicleControllerResponseDTO>> getVehiclesByAdministrativeStatus(@PathVariable String administrativeStatus){

        AdministrativeStatus administrativeStatusEnum = AdministrativeStatus.valueOf(administrativeStatus.toUpperCase());

        List<VehicleResponse> vehicles = getVehiclesByAdministrativeStatus.execute(administrativeStatusEnum);

        List<VehicleControllerResponseDTO> response =  vehicles.stream()
                .map(vehicleControllerMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Void> updateVehicle(
            @PathVariable UUID id,
            @RequestBody UpdateVehicleControllerRequestDTO request) {

        UpdateVehicleCommand command = vehicleControllerMapper.toUpdate(request);

        updateVehicleByIdUseCase.update(command, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteVehicleById(@PathVariable UUID id) {
        deleteVehicleByIdUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/sell")
    public ResponseEntity<Void> sellVehicle(@PathVariable UUID id) {

        markVehicleAsSoldUseCase.markVehicleAsSold(id);

        return ResponseEntity.noContent().build();
    }


}