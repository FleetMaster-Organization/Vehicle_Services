package com.services.vehicle.infrastructure.web.controller;

import com.services.vehicle.application.dto.*;
import com.services.vehicle.application.port.in.*;
import com.services.vehicle.application.usecase.UpdateDocumentStatusService;
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
    private final DeleteVehicleByIdUseCase deleteVehicleByIdUseCase;
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


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
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

    @PostMapping("/{vehicleId}/documents")
//    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<CreateVehicleControllerResponseDTO> addDocument(
            @PathVariable UUID vehicleId,
            @RequestBody CreateVehicleDocumentRequestDTO request
    ) {

        UUID documentId = addDocumentToVehicleUseCase.addDocument(
                vehicleId,
                vehicleDocumentMapper.toCommand(request)
        );

        CreateVehicleControllerResponseDTO response = new CreateVehicleControllerResponseDTO(
                documentId,
                "Documento creado correctamente"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_COORDINADOR')('ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleById(@PathVariable UUID id) {

        VehicleResponse response = getVehicleByIdUseCase.execute(id);

        return ResponseEntity.ok(
                vehicleControllerMapper.toDto(response)
        );
    }

    @GetMapping("/{vehicleId}/documents/{documentId}")
    public ResponseEntity<DocumentControllerResponseDTO> getDocumentById(
            @PathVariable UUID vehicleId,
            @PathVariable UUID documentId
    ) {

        DocumentResponse response =
                getDocumentByIdUseCase.execute(vehicleId, documentId);

        return ResponseEntity.ok(vehicleDocumentMapper.toResponse(response));
    }

    @GetMapping("/{vehicleId}/documents")
    public ResponseEntity<List<DocumentControllerResponseDTO>> getAllDocumentsByVehicleId(@PathVariable UUID vehicleId) {
        List<DocumentResponse> documents = getAllDocumentsByVehicleUseCase.execute(vehicleId);

        List<DocumentControllerResponseDTO> response = documents.stream()
                .map(vehicleDocumentMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/documentsAboutToExpire")
    public ResponseEntity<List<DocumentControllerResponseDTO>>  documentsAboutToExpire(@PathVariable UUID id) {

        List<DocumentResponse> documents = documentsAboutToExpireUseCase.execute(id);

        List<DocumentControllerResponseDTO> response = documents.stream()
                .map(vehicleDocumentMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_COORDINADOR')('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<VehicleControllerResponseDTO>> getAllVehicles() {

        List<VehicleResponse> vehicles = getAllVehiclesUseCase.execute();

        List<VehicleControllerResponseDTO> response = vehicles.stream()
                .map(vehicleControllerMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{plate}/plate")
//    @PreAuthorize("hasAuthority('ROLE_COORDINADOR')('ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehicleByPlate(@PathVariable String plate){

        LicensePlate licensePlate = new LicensePlate(plate);

        VehicleResponse response = getVehicleByPlateUseCase.execute(licensePlate);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }

    @GetMapping("{vin}/vin")
//    @PreAuthorize("hasAuthority('ROLE_COORDINADOR')('ROLE_ADMINISTRATOR')")
    public ResponseEntity<VehicleControllerResponseDTO> getVehiclesByVin(@PathVariable String vin){

        Vin vinNumber = new Vin(vin);

        VehicleResponse response = getVehiclesByVinUseCase.execute(vinNumber);

        return ResponseEntity.ok(vehicleControllerMapper.toDto(response));
    }

    @GetMapping("/{operationalStatus}/operational-status")
//    @PreAuthorize("hasAuthority('ROLE_COORDINADOR')('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<VehicleControllerResponseDTO>> getVehiclesByOperationalStatus(@PathVariable String operationalStatus){

        OperationalStatus operationalStatusEnum = OperationalStatus.valueOf(operationalStatus.toUpperCase());

        List<VehicleResponse> vehicles = getVehiclesByOperationalStatus.execute(operationalStatusEnum);

        List<VehicleControllerResponseDTO> response = vehicles.stream()
                .map(vehicleControllerMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{administrativeStatus}/administrative-status")
//    @PreAuthorize("hasAuthority('ROLE_COORDINADOR')('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<VehicleControllerResponseDTO>> getVehiclesByAdministrativeStatus(@PathVariable String administrativeStatus){

        AdministrativeStatus administrativeStatusEnum = AdministrativeStatus.valueOf(administrativeStatus.toUpperCase());

        List<VehicleResponse> vehicles = getVehiclesByAdministrativeStatus.execute(administrativeStatusEnum);

        List<VehicleControllerResponseDTO> response =  vehicles.stream()
                .map(vehicleControllerMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update")
//    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> updateVehicle(
            @PathVariable UUID id,
            @RequestBody UpdateVehicleControllerRequestDTO request) {

        UpdateVehicleCommand command = vehicleControllerMapper.toUpdate(request);

        updateVehicleByIdUseCase.update(command, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete")
//    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> deleteVehicleById(@PathVariable UUID id) {
        deleteVehicleByIdUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/sell")
//    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> sellVehicle(@PathVariable UUID id) {

        markVehicleAsSoldUseCase.markVehicleAsSold(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
//    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> activateVehicle(@PathVariable UUID id) {

        activateVehicleUseCase.activate(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/send-maintenance")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> sendMaintenance(@PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String user = (auth != null) ? auth.getName() : "SYSTEM";

        sendVehicleToMaintenanceUseCase.sendVehicleToMaintenance(id, user);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Void> assignVehicle(@PathVariable UUID id) {
        assignVehicleUseCase.assign(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/release")
    public ResponseEntity<Void> releaseVehicle(@PathVariable UUID id) {
        releaseVehicleUseCase.release(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/documents/status")
    public ResponseEntity<Void> updateDocumentStatus(@PathVariable UUID id){
        updateDocumentsStatusUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{vehicleId}/document/{documentId}/renew")
    public ResponseEntity<Void> renewDocument(@PathVariable UUID vehicleId, @PathVariable UUID documentId,
                                              @RequestBody RenewVehicleControllerRequestDTO request) {

        RenewVehicleDocumentCommand document = vehicleDocumentMapper.toUpdate(request);

        renewDocumentUseCase.renewDocument(vehicleId,documentId,document);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<Void> suspend(
            @PathVariable UUID id,
            @RequestBody SuspendVehicleRequest request) {

        suspendVehicleUseCase.suspend(id, request.suspensionReason(), request.modifiedBy() != null ? request.modifiedBy() : "SYSTEM_USER");
        return ResponseEntity.noContent().build();
    }



}