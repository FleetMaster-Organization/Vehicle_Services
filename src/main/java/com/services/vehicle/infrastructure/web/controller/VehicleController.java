package com.services.vehicle.infrastructure.web.controller;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.dto.VehicleResponse;
import com.services.vehicle.application.port.in.CreateVehicleUseCase;
import com.services.vehicle.application.port.in.GetAllVehiclesUseCase;
import com.services.vehicle.application.port.in.GetVehicleByIdUseCase;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleControllerMapper vehicleControllerMapper;
    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetVehicleByIdUseCase getVehicleByIdUseCase;
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

}