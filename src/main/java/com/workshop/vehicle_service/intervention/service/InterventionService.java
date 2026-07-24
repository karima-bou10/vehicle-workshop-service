package com.workshop.vehicle_service.intervention.service;

import com.workshop.vehicle_service.intervention.dtos.InterventionRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;

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

    InterventionResponse modifierUneIntervention(InterventionRequest interventionRequest);

    InterventionResponse enregistrerUneIntervention(InterventionRequest interventionRequest);

    InterventionResponse supprimerUneIntervention(Long idIntervention);
}
