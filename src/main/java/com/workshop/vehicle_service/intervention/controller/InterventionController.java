package com.workshop.vehicle_service.intervention.controller;

import com.workshop.vehicle_service.intervention.dtos.*;
import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;
import com.workshop.vehicle_service.intervention.service.Imp.BusinessException;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.intervention.service.StatutInterventionService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intervention")
@AllArgsConstructor
public class InterventionController {

    private InterventionService interventionService;
    private StatutInterventionService statutInterventionService;


    /**
     * Récupérer toutes les interventions
     */
    @PermitAll
    @GetMapping
    public List<InterventionResponse> recupererListInterventions() {
        return interventionService.recupererListInterventions();
    }

    @GetMapping("/search")
    public List<InterventionResponse> rechercherInterventions(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String immatriculation,
            @RequestParam(required = false) StatutIntervention statut,
            @RequestParam(required = false) Priorite priorite,
            @RequestParam(required = false) TypeIntervention typeIntervention,
            @RequestParam(required = false) Long vehiculeId,
            @RequestParam(required = false) Long mecanicienId) {

        InterventionSearchRequest request = new InterventionSearchRequest(
                reference,
                immatriculation,
                statut,
                priorite,
                typeIntervention,
                vehiculeId,
                mecanicienId
        );

        return interventionService.rechercherInterventions(request);
    }

    @GetMapping("/vehicules/{vehiculeId}/interventions")
    public List<InterventionResponse> listInterventionsByVehiculeId(@PathVariable Long vehiculeId) {
        return interventionService.listInterventionsByVehiculeId(vehiculeId);
    }

    @GetMapping("/mecaniciens/{mecanicienId}/interventions")
    public List<InterventionResponse> listInterventionsByMecanicienId(@PathVariable Long mecanicienId) {
        return interventionService.listInterventionsByMecanicienId(mecanicienId);
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
    @PostMapping("/new")
    public InterventionResponse enregistrerUneIntervention(
            @RequestBody InterventionCreationRequest interventionRequest) {
        return interventionService.enregistrerUneIntervention(interventionRequest);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping("/{id}/affecter")
    public InterventionResponse affecterMecanicien(
            @PathVariable Long id,
            @RequestBody AffectationMecanicienRequest request) throws BusinessException {

        return interventionService.affectationMecanicienIntervention(id, request);
    }

    /**
     * Modifier une intervention
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping("/{id}/edit")
    public InterventionResponse modifierUneIntervention(
            @PathVariable Long id,
            @RequestBody InterventionUpdateRequest request) throws BusinessException {

        return interventionService.modifierUneIntervention(request, id);

    }


    /**
     * Supprimer une intervention
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")

    @DeleteMapping("/{id}")

    public InterventionResponse supprimerUneIntervention(

    @PathVariable Long id) {

        return interventionService.supprimerUneIntervention(id);

    }


    /**
     * Ajouter Diagnostic
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping("/{id}/diagnostic")
    public InterventionResponse ajouterDiagnostic(
            @PathVariable Long id,
            @RequestBody DiagnosticRequest request) throws BusinessException {

        return interventionService.ajouterDiagnostic(id, request);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping("/{id}/devis")
    public InterventionResponse ajouterDevis(
            @PathVariable Long id,
            @RequestBody DevisRequest request) {

        return interventionService.ajouterDevis(id, request);
    }


    @PutMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<InterventionResponse> changerStatut(
            @PathVariable Long id,
            @RequestBody @Valid ChangementStatutRequest request) {

      InterventionResponse interventionResponse =
              interventionService.changerStatut(id, request);
        return ResponseEntity.ok(interventionResponse);
    }

    @PermitAll
    @GetMapping("/historique")
    public List<InterventionResponse> recupererHistoriqueInterventions() {
        return interventionService.recupererHistoriqueComplet();
    }



    @GetMapping("/retards")

    public List<InterventionResponse> getInterventionsEnRetard() {

        return interventionService.getInterventionsRestitueEnRetard();

    }
}
















