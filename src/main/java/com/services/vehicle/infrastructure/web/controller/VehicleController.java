package com.services.vehicle.infrastructure.web.controller;

import com.services.vehicle.application.dto.CreateVehicleCommand;
import com.services.vehicle.application.port.in.CreateVehicleUseCase;
import com.services.vehicle.domain.model.Vehicle;
import com.services.vehicle.infrastructure.web.dto.VehicleRequestDTO;
import com.services.vehicle.infrastructure.web.dto.VehicleResponseDTO;
import com.services.vehicle.infrastructure.web.mapper.VehicleControllerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleControllerMapper mapper;
    private final CreateVehicleUseCase createVehicleUseCase;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(
            @RequestBody VehicleRequestDTO request) {

        CreateVehicleCommand command = mapper.toCommand(request);

        UUID id = createVehicleUseCase.create(command);

        VehicleResponseDTO response = new VehicleResponseDTO(
                id,
                "Vehículo creado correctamente"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}