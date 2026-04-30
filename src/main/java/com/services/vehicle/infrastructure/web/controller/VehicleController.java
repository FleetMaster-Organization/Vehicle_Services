package com.services.vehicle.infrastructure.web.controller;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.port.in.*;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.infrastructure.web.dto.CreateVehicleControllerResponseDTO;
import com.services.vehicle.infrastructure.web.dto.VehicleControllerRequestDTO;
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

    @PostMapping
    public ResponseEntity<CreateVehicleControllerResponseDTO> createVehicle(
            @RequestBody VehicleControllerRequestDTO request) {

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

    @GetMapping("/plate/{plate}")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleByPlate(@PathVariable String plate){

        LicensePlate licensePlate = new LicensePlate(plate);

        VehicleResponse response = getVehicleByPlateUseCase.execute(licensePlate);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }

    @GetMapping("/vin/{vin}")
    public ResponseEntity<VehicleControllerResponseDTO> getVehiclesByVin(@PathVariable String vin){

        Vin vinNumber = new Vin(vin);

        VehicleResponse response = getVehiclesByVinUseCase.execute(vinNumber);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }


}