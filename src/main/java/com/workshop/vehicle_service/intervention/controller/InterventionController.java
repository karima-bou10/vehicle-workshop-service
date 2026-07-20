package com.workshop.vehicle_service.intervention.controller;

import com.workshop.vehicle_service.intervention.dtos.InterventionRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intervention")
@AllArgsConstructor
public class InterventionController {

    private InterventionService interventionService;


    /**
     * Récupérer toutes les interventions
     */
    @GetMapping
    public List<InterventionResponse> recupererListInterventions() {
        return interventionService.recupererListInterventions();
    }


    /**
     * Récupérer une intervention par son ID
     */
    @GetMapping("/{id}")
    public InterventionResponse recupererUneIntervention(@PathVariable int id) {
        return interventionService.recupererUneIntervention(id);
    }

    /**
     * Enregistrer une nouvelle intervention
     */
    @PostMapping
    public InterventionResponse enregistrerUneIntervention(
            @RequestBody InterventionRequest interventionRequest) {
        return interventionService.EnregistrerUneIntervention(interventionRequest);
    }



    /**
     * Modifier une intervention
     */
    @PutMapping
    public InterventionResponse modifierUneIntervention(
            @RequestBody InterventionRequest interventionRequest) {
        return interventionService.modifierUneIntervention(interventionRequest);
    }



    /**
     * Supprimer une intervention
     */
    @DeleteMapping
    public InterventionResponse supprimerUneIntervention(
            @RequestBody InterventionRequest interventionRequest) {
        return interventionService.supprimerUneIntervention(interventionRequest);
    }










}
