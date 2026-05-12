package com.services.vehicle.infrastructure.web.controller;

import com.services.vehicle.application.dto.*;
import com.services.vehicle.application.port.in.*;
import com.services.vehicle.domain.enums.AdministrativeStatus;
import com.services.vehicle.domain.enums.OperationalStatus;
import com.services.vehicle.domain.valueobject.LicensePlate;
import com.services.vehicle.domain.valueobject.Vin;
import com.services.vehicle.infrastructure.web.dto.*;
import com.services.vehicle.infrastructure.web.mapper.VehicleControllerMapper;
import com.services.vehicle.infrastructure.web.mapper.VehicleDocumentControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleControllerMapper vehicleControllerMapper;
    private final VehicleDocumentControllerMapper vehicleDocumentMapper;
    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetVehicleByIdUseCase getVehicleByIdUseCase;
    private final GetVehiclesByPlateUseCase getVehicleByPlateUseCase;
    private final GetVehiclesByVinUseCase  getVehiclesByVinUseCase;
    private final GetAllVehiclesUseCase getAllVehiclesUseCase;
    private final GetAllVehiclesByAdministrativeStatusUseCase getVehiclesByAdministrativeStatus;
    private final GetAllVehiclesByOperationalStatusUseCase getVehiclesByOperationalStatus;
    private final UpdateVehicleByIdUseCase updateVehicleByIdUseCase;
    private final ScrapVehicleByIdUseCase scrapVehicleByIdUseCase;
    private final MarkVehicleAsSoldUseCase markVehicleAsSoldUseCase;
    private final ActivateVehicleUseCase activateVehicleUseCase;
    private final AddDocumentToVehicleUseCase addDocumentToVehicleUseCase;
    private final GetDocumentByIdUseCase getDocumentByIdUseCase;
    private final GetAllDocumentsByVehicleUseCase getAllDocumentsByVehicleUseCase;
    private final RenewDocumentUseCase renewDocumentUseCase;
    private final SendVehicleToMaintenanceUseCase sendVehicleToMaintenanceUseCase;
    private final AssignVehicleUseCase assignVehicleUseCase;
    private final ReleaseVehicleUseCase releaseVehicleUseCase;
    private final DocumentsAboutToExpireUseCase documentsAboutToExpireUseCase;
    private final UpdateDocumentsStatusUseCase  updateDocumentsStatusUseCase;
    private final SuspendVehicleUseCase suspendVehicleUseCase;

    String defaultUser = "SYSTEM";

    @Operation(
            summary = "Crear un nuevo vehículo",
            description = "Crea un vehículo en el sistema. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vehículo creado correctamente",
                            content = @Content(schema = @Schema(implementation = CreateVehicleControllerResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<CreateVehicleControllerResponseDTO> createVehicle(
            @RequestBody CreateVehicleControllerRequestDTO request) {

        CreateVehicleCommand command = vehicleControllerMapper.toCommand(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;

        UUID id = createVehicleUseCase.create(command, user);

        CreateVehicleControllerResponseDTO response = new CreateVehicleControllerResponseDTO(
                id,
                "Vehículo creado correctamente"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Agregar documento a un vehículo",
            description = "Asocia un nuevo documento a un vehículo existente. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Documento creado correctamente",
                            content = @Content(schema = @Schema(implementation = CreateVehicleControllerResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PostMapping("/{vehicleId}/documents")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<CreateVehicleControllerResponseDTO> addDocument(
            @PathVariable UUID vehicleId,
            @RequestBody CreateVehicleDocumentRequestDTO request
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;

        UUID documentId = addDocumentToVehicleUseCase.addDocument(
                vehicleId,
                vehicleDocumentMapper.toCommand(request)
                ,user
        );

        CreateVehicleControllerResponseDTO response = new CreateVehicleControllerResponseDTO(
                documentId,
                "Documento creado correctamente"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Obtener vehículo por ID",
            description = "Retorna un vehículo por su UUID. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
                            content = @Content(schema = @Schema(implementation = VehicleControllerResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleById(@PathVariable UUID id) {

        VehicleResponse response = getVehicleByIdUseCase.execute(id);

        return ResponseEntity.ok(
                vehicleControllerMapper.toDto(response)
        );
    }

    @Operation(
            summary = "Listar vehículos",
            description = "Retorna todos los vehículos. Se puede filtrar por estado operacional o administrativo. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida correctamente",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = VehicleControllerResponseDTO.class)))),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<VehicleControllerResponseDTO>> getVehicles(

            @RequestParam(required = false)
            OperationalStatus operationalStatus,

            @RequestParam(required = false)
            AdministrativeStatus administrativeStatus
    ) {

        List<VehicleResponse> vehicles;

        if (operationalStatus != null) {

            vehicles = getVehiclesByOperationalStatus
                    .execute(operationalStatus);

        } else if (administrativeStatus != null) {

            vehicles = getVehiclesByAdministrativeStatus
                    .execute(administrativeStatus);

        } else {

            vehicles = getAllVehiclesUseCase.execute();
        }

        List<VehicleControllerResponseDTO> response =
                vehicles.stream()
                        .map(vehicleControllerMapper::toDto)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener documento por ID",
            description = "Retorna un documento específico de un vehículo. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento encontrado",
                            content = @Content(schema = @Schema(implementation = DocumentControllerResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo o documento no encontrado")
            }
    )
    @GetMapping("/{vehicleId}/documents/{documentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<DocumentControllerResponseDTO> getDocumentById(
            @PathVariable UUID vehicleId,
            @PathVariable UUID documentId
    ) {

        DocumentResponse response =
                getDocumentByIdUseCase.execute(vehicleId, documentId);

        return ResponseEntity.ok(vehicleDocumentMapper.toResponse(response));
    }

    @Operation(
            summary = "Listar documentos de un vehículo",
            description = "Retorna todos los documentos asociados a un vehículo. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de documentos obtenida correctamente",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentControllerResponseDTO.class)))),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @GetMapping("/{vehicleId}/documents")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<DocumentControllerResponseDTO>> getAllDocumentsByVehicleId(@PathVariable UUID vehicleId) {
        List<DocumentResponse> documents = getAllDocumentsByVehicleUseCase.execute(vehicleId);

        List<DocumentControllerResponseDTO> response = documents.stream()
                .map(vehicleDocumentMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener documentos próximos a vencer",
            description = "Retorna los documentos de un vehículo que están próximos a vencer. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de documentos próximos a vencer",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentControllerResponseDTO.class)))),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @GetMapping("/{id}/documentsAboutToExpire")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<DocumentControllerResponseDTO>>  documentsAboutToExpire(@PathVariable UUID id) {

        List<DocumentResponse> documents = documentsAboutToExpireUseCase.execute(id);

        List<DocumentControllerResponseDTO> response = documents.stream()
                .map(vehicleDocumentMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener vehículo por placa",
            description = "Retorna un vehículo buscando por su número de placa. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
                            content = @Content(schema = @Schema(implementation = VehicleControllerResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @GetMapping("/{plate}/plate")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleByPlate(@PathVariable String plate){

        LicensePlate licensePlate = new LicensePlate(plate);

        VehicleResponse response = getVehicleByPlateUseCase.execute(licensePlate);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }

    @Operation(
            summary = "Obtener vehículo por VIN",
            description = "Retorna un vehículo buscando por su número VIN. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
                            content = @Content(schema = @Schema(implementation = VehicleControllerResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @GetMapping("{vin}/vin")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehiclesByVin(@PathVariable String vin){

        Vin vinNumber = new Vin(vin);

        VehicleResponse response = getVehiclesByVinUseCase.execute(vinNumber);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }


    @Operation(
            summary = "Actualizar un vehículo",
            description = "Actualiza los datos de un vehículo existente. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo actualizado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> updateVehicle(
            @PathVariable UUID id,
            @RequestBody UpdateVehicleControllerRequestDTO request) {

        UpdateVehicleCommand command = vehicleControllerMapper.toUpdate(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;

        updateVehicleByIdUseCase.update(command, id, user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Dar de baja un vehículo (scrap)",
            description = "Marca un vehículo como dado de baja. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo dado de baja correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PatchMapping("/{id}/scrap")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> scrapVehicleById(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        scrapVehicleByIdUseCase.scrap(id,user);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Marcar vehículo como vendido",
            description = "Cambia el estado de un vehículo a vendido. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo marcado como vendido correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PatchMapping("/{id}/sell")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> sellVehicle(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        markVehicleAsSoldUseCase.markVehicleAsSold(id,user);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Activar un vehículo",
            description = "Cambia el estado de un vehículo a activo. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo activado correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> activateVehicle(@PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        activateVehicleUseCase.activate(id,user);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Enviar vehículo a mantenimiento",
            description = "Cambia el estado de un vehículo a en mantenimiento. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo enviado a mantenimiento correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PatchMapping("/{id}/send-maintenance")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> sendMaintenance(@PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String user = (auth != null) ? auth.getName() : defaultUser;

        sendVehicleToMaintenanceUseCase.sendVehicleToMaintenance(id, user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Asignar vehículo",
            description = "Marca un vehículo como asignado. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo asignado correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> assignVehicle(@PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        assignVehicleUseCase.assign(id, user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Liberar vehículo",
            description = "Marca un vehículo como disponible. Requiere rol ROLE_COORDINADOR o ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo liberado correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PatchMapping("/{id}/release")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> releaseVehicle(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        releaseVehicleUseCase.release(id,user);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar estado de documentos del vehículo",
            description = "Actualiza el estado de todos los documentos asociados a un vehículo. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Estado de documentos actualizado correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PatchMapping("/{id}/documents/status")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> updateDocumentStatus(@PathVariable UUID id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        updateDocumentsStatusUseCase.execute(id,user);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Renovar documento de un vehículo",
            description = "Renueva un documento específico asociado a un vehículo. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Documento renovado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo o documento no encontrado")
            }
    )

    @PatchMapping("/{vehicleId}/document/{documentId}/renew")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> renewDocument(@PathVariable UUID vehicleId, @PathVariable UUID documentId,
                                              @RequestBody RenewVehicleControllerRequestDTO request) {

        RenewVehicleDocumentCommand document = vehicleDocumentMapper.toUpdate(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String user = (auth != null) ? auth.getName() : defaultUser;

        renewDocumentUseCase.renewDocument(vehicleId,documentId,document,user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Suspender un vehículo",
            description = "Suspende un vehículo indicando el motivo de suspensión. Requiere el rol ROLE_ADMINISTRATOR.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehículo suspendido correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
            }
    )
    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> suspend(
            @PathVariable UUID id,
            @RequestBody SuspendVehicleRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String user = (auth != null) ? auth.getName() : defaultUser;

        suspendVehicleUseCase.suspend(id, request.suspensionReason(), user);
        return ResponseEntity.noContent().build();
    }



}