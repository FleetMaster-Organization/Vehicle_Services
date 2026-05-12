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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleById(@PathVariable UUID id) {

        VehicleResponse response = getVehicleByIdUseCase.execute(id);

        return ResponseEntity.ok(
                vehicleControllerMapper.toDto(response)
        );
    }

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

    @GetMapping("/{vehicleId}/documents")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<DocumentControllerResponseDTO>> getAllDocumentsByVehicleId(@PathVariable UUID vehicleId) {
        List<DocumentResponse> documents = getAllDocumentsByVehicleUseCase.execute(vehicleId);

        List<DocumentControllerResponseDTO> response = documents.stream()
                .map(vehicleDocumentMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/documentsAboutToExpire")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<DocumentControllerResponseDTO>>  documentsAboutToExpire(@PathVariable UUID id) {

        List<DocumentResponse> documents = documentsAboutToExpireUseCase.execute(id);

        List<DocumentControllerResponseDTO> response = documents.stream()
                .map(vehicleDocumentMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{plate}/plate")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleByPlate(@PathVariable String plate){

        LicensePlate licensePlate = new LicensePlate(plate);

        VehicleResponse response = getVehicleByPlateUseCase.execute(licensePlate);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }

    @GetMapping("{vin}/vin")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehiclesByVin(@PathVariable String vin){

        Vin vinNumber = new Vin(vin);

        VehicleResponse response = getVehiclesByVinUseCase.execute(vinNumber);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }



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

    @PatchMapping("/{id}/scrap")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> scrapVehicleById(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        scrapVehicleByIdUseCase.scrap(id,user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/sell")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> sellVehicle(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        markVehicleAsSoldUseCase.markVehicleAsSold(id,user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> activateVehicle(@PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        activateVehicleUseCase.activate(id,user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/send-maintenance")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> sendMaintenance(@PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String user = (auth != null) ? auth.getName() : defaultUser;

        sendVehicleToMaintenanceUseCase.sendVehicleToMaintenance(id, user);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> assignVehicle(@PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        assignVehicleUseCase.assign(id, user);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/release")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINADOR', 'ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> releaseVehicle(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        releaseVehicleUseCase.release(id,user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/documents/status")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> updateDocumentStatus(@PathVariable UUID id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;
        updateDocumentsStatusUseCase.execute(id,user);
        return ResponseEntity.noContent().build();
    }

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