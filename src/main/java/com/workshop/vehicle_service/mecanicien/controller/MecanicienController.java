package com.workshop.vehicle_service.mecanicien.controller;

import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.service.MecanicienService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mecaniciens")
@AllArgsConstructor
public class MecanicienController {

    private final MecanicienService mecanicienService;

    @GetMapping("/getAll")
    public ResponseEntity<List<MecanicienResponse>> getAllMecaniciens(){
        return ResponseEntity.ok(mecanicienService.getAllMecaniciens());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MecanicienResponse> getMecanicien(@PathVariable Long id){
        return ResponseEntity.ok(mecanicienService.getMecanicienById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<MecanicienResponse> createMecanicien(@RequestBody CreateMecanicienRequest request){
        MecanicienResponse response = mecanicienService.saveMecanicien(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MecanicienResponse> updateMecanicien(@PathVariable Long id, @RequestBody UpdateMecanicienRequest request) {
        MecanicienResponse updatedResponse = mecanicienService.updateMecanicien(id, request);
        return ResponseEntity.ok(updatedResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMecanicien(@PathVariable Long id){
        try {
            mecanicienService.deleteMecanicienById(id);
            return ResponseEntity.ok("Mecanicien deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting mecanicien: " + e.getMessage());
        }
    }

    @GetMapping("/getAllDisponibles")
    public ResponseEntity<List<MecanicienResponse>> getMecaniciensDisponibles(){
        return ResponseEntity.ok(mecanicienService.getMecaniciensDisponibles());
    }
}
