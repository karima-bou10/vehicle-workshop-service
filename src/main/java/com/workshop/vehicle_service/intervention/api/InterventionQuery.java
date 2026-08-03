package com.workshop.vehicle_service.intervention.api;

import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;

import java.util.List;

/**
 * Cette interface définit le contrat pour l'interrogation des interventions dans l'application de service véhicule.
 * Elle peut être implémentée pour fournir diverses méthodes de récupération des données d'intervention selon différents critères.
 */
public interface InterventionQuery {


    /**
     * Récupère toutes les interventions associées à un véhicule spécifique.
     *
     * @param vehiculeId l'identifiant du véhicule dont on souhaite récupérer les interventions
     * @return une liste d'interventions associées au véhicule spécifié
     */
    List<InterventionResponse> listInterventionsByVehiculeId(Long vehiculeId);


}
