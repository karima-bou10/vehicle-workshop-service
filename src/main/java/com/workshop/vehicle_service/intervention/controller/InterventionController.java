package com.workshop.vehicle_service.intervention.controller;

import com.workshop.vehicle_service.intervention.dtos.*;
import com.workshop.vehicle_service.intervention.service.Imp.BusinessException;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PermitAll
    @GetMapping
    public List<InterventionResponse> recupererListInterventions() {
        return interventionService.recupererListInterventions();
    }


    /**
     * Récupérer une intervention par son ID
     */
    @GetMapping("/{id}")
    public InterventionResponse recupererUneIntervention(@PathVariable Long id) {
        return interventionService.recupererUneIntervention(id);
    }

    /**
     * Enregistrer une nouvelle intervention
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PostMapping("/new")
    public InterventionResponse enregistrerUneIntervention(
            @RequestBody InterventionCreationRequest interventionRequest) {
        return interventionService.enregistrerUneIntervention(interventionRequest);
    }


    @PutMapping("/{id}/affecter")
    public InterventionResponse affecterMecanicien(
            @PathVariable Long id,
            @RequestBody AffectationMecanicienRequest request) throws BusinessException {

        return interventionService.affectationMecanicienIntervention(id, request);
    }

    /**
     * Modifier une intervention
     */
    @PutMapping("/{id}")
    public InterventionResponse modifierUneIntervention(
            @PathVariable Long idIntervention,
            @RequestBody InterventionUpdateRequest request) {

            return interventionService.modifierUneIntervention(request, idIntervention);
    }


    /**
     * Supprimer une intervention
     */
    @DeleteMapping("/id")
    public InterventionResponse supprimerUneIntervention(
            @PathVariable Long idIntervention) {
        return interventionService.supprimerUneIntervention(idIntervention);
    }


    /**
     * Ajouter Diagnostic
     */
    @PutMapping("/{id}/diagnostic")
    public InterventionResponse ajouterDiagnostic(
            @PathVariable Long id,
            @RequestBody DiagnosticRequest request) {

        return interventionService.ajouterDiagnostic(id, request);
    }

    @PutMapping("/{id}/devis")
    public InterventionResponse ajouterDevis(
            @PathVariable Long id,
            @RequestBody DevisRequest request) {

        return interventionService.ajouterDevis(id, request);
    }












}
