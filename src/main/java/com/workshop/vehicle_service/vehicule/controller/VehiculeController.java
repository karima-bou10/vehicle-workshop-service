package com.workshop.vehicle_service.vehicule.controller;

import com.workshop.vehicle_service.vehicule.dto.VehiculeRequest;
import com.workshop.vehicle_service.vehicule.dto.VehiculeResponse;
import com.workshop.vehicle_service.vehicule.service.VehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {
    private final VehiculeService vehiculeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<Page<VehiculeResponse>> getAllVehicules(Pageable pageable){
        return ResponseEntity.ok(vehiculeService.getAllVehicules(pageable));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<VehiculeResponse> getVehiculeById(@PathVariable Long id){
         return ResponseEntity.ok(vehiculeService.getVehiculeById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<VehiculeResponse> createVehicule(@Valid @RequestBody VehiculeRequest vehiculeRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.createVehicule(vehiculeRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public  ResponseEntity<VehiculeResponse> updateVehicule(@PathVariable Long id, @Valid @RequestBody VehiculeRequest vehiculeRequest){
           return ResponseEntity.ok(vehiculeService.updateVehicule(id, vehiculeRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
               vehiculeService.deleteVehicule(id);
               return ResponseEntity.noContent().build();
    }

}
