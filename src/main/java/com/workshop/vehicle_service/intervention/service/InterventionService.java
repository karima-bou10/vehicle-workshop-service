package com.workshop.vehicle_service.intervention.service;

import com.workshop.vehicle_service.intervention.dtos.*;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.service.Imp.BusinessException;
import jakarta.validation.Valid;

/**
 * <h1>Intervention Service</h1>
 * <ul>
 * <li>enregistrerUneIntervension</li>
 * <li>chercherUneListDesIntervention</li>
 * <li>nombreTotalDesIntervention</li>
 * <li>ajouterLiens</li>
 * <li>recupererUneIntervention</li>
 * <li>modifierUneIntervention</li>
 * <li>supprimerUneIntervention</li>
 * </ul>
 */

import java.util.List;

public interface InterventionService {

    List<InterventionResponse> recupererListInterventions();

    InterventionResponse recupererUneIntervention(Long idIntervention);

    InterventionResponse modifierUneIntervention(InterventionUpdateRequest interventionUpdateRequest, Long idIntervention);

    InterventionResponse enregistrerUneIntervention(InterventionCreationRequest interventionRequest);

    InterventionResponse supprimerUneIntervention(Long idIntervention);

    InterventionResponse affectationMecanicienIntervention(Long interventionId, AffectationMecanicienRequest request) throws BusinessException;
    InterventionResponse ajouterDiagnostic(Long interventionId, DiagnosticRequest request);
    InterventionResponse ajouterDevis(Long interventionId, DevisRequest request);


    InterventionResponse changerStatut(Long id, @Valid ChangementStatutRequest request);
    List<InterventionResponse> listInterventionsByVehiculeId( Long vehiculeId);
}
