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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @param pageable
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping
    public Page<InterventionResponse> recupererListInterventions(@ParameterObject Pageable pageable) {
        return interventionService.recupererListInterventions(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/search")
    public Page<InterventionResponse> rechercherInterventions(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String immatriculation,
            @RequestParam(required = false) StatutIntervention statut,
            @RequestParam(required = false) Priorite priorite,
            @RequestParam(required = false) TypeIntervention typeIntervention,
            @RequestParam(required = false) Long vehiculeId,
            @RequestParam(required = false) Long mecanicienId,
            @RequestParam(defaultValue = "false") boolean includeArchived,
            @ParameterObject Pageable pageable) {

        InterventionSearchRequest request =
                new InterventionSearchRequest(
                        reference,
                        immatriculation,
                        statut,
                        priorite,
                        typeIntervention,
                        vehiculeId,
                        mecanicienId
                );

        return interventionService.rechercherInterventions(
                request,
                pageable,includeArchived
        );
    }

    /**
     * ListeIntervention par vehicule
      * @param vehiculeId
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/vehicules/{vehiculeId}/interventions")
    public List<InterventionResponse> listInterventionsByVehiculeId(@PathVariable Long vehiculeId) {
        return interventionService.listInterventionsByVehiculeId(vehiculeId);
    }

    /***
     * Liste intervention par mecanicien
     * @param mecanicienId
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/mecaniciens/{mecanicienId}/interventions")
    public List<InterventionResponse> listInterventionsByMecanicienId(@PathVariable Long mecanicienId) {
        return interventionService.listInterventionsByMecanicienId(mecanicienId);
    }


    /***
     * Récupérer une intervention par son ID
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/{id}")
    public InterventionResponse recupererUneIntervention(@PathVariable Long id) {
        return interventionService.recupererUneIntervention(id);
    }

    /***
     * Enregistrer une nouvelle intervention
     * @param interventionRequest
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @PostMapping("/new")
    public InterventionResponse enregistrerUneIntervention(
            @RequestBody InterventionCreationRequest interventionRequest) {
        return interventionService.enregistrerUneIntervention(interventionRequest);
    }

    /***
     * Affecter un mecanicien
      * @param id
     * @param request
     * @return
     * @throws BusinessException
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping("/{id}/affecter")
    public InterventionResponse affecterMecanicien(
            @PathVariable Long id,
            @RequestBody AffectationMecanicienRequest request) throws BusinessException {

        return interventionService.affectationMecanicienIntervention(id, request);
    }

    /***
     * Modifier une intervention
     * @param id
     * @param request
     * @return
     * @throws BusinessException
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @PutMapping("/{id}/edit")
    public InterventionResponse modifierUneIntervention(
            @PathVariable Long id,
            @RequestBody InterventionUpdateRequest request) throws BusinessException {

        return interventionService.modifierUneIntervention(request, id);

    }


    /***
     * Archiver une intervention (soft delete)
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")

    @DeleteMapping("/{id}")

    public InterventionResponse archiverUneUneIntervention(

    @PathVariable Long id) {

        return interventionService.archiveUneIntervention(id);

    }


    /***
     * Ajouter diagnostic
     * @param id
     * @param request
     * @return
     * @throws BusinessException
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @PutMapping("/{id}/diagnostic")
    public InterventionResponse ajouterDiagnostic(
            @PathVariable Long id,
            @RequestBody DiagnosticRequest request) throws BusinessException {

        return interventionService.ajouterDiagnostic(id, request);
    }

    /**
     * Ajouter devis
     * @param id
     * @param request
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @PutMapping("/{id}/devis")
    public InterventionResponse ajouterDevis(
            @PathVariable Long id,
            @RequestBody DevisRequest request) {

        return interventionService.ajouterDevis(id, request);
    }

    /**
     * Changer le status d'une intervention
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<InterventionResponse> changerStatut(
            @PathVariable Long id,
            @RequestBody @Valid ChangementStatutRequest request) {

      InterventionResponse interventionResponse =
              interventionService.changerStatut(id, request);
        return ResponseEntity.ok(interventionResponse);
    }

    /**
     * Recuperer historique intervention
     *
     *
     * @return
     */

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/historique")
    public Page<InterventionResponse> recupererHistoriqueInterventions(@ParameterObject Pageable pageable) {
        return interventionService.recupererHistoriqueComplet(pageable);
    }

    /**
     * Intervention en retards
     *
     * @return
     */

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/retards")

    public Page<InterventionResponse> getInterventionsEnRetard(@ParameterObject Pageable pageable) {

        return interventionService.getInterventionsRestitueEnRetard(pageable);

    }
}
















