package com.workshop.vehicle_service.intervention.dtos;

import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;

public record InterventionSearchRequest(
        String reference,
        String immatriculation,
        StatutIntervention statut,
        Priorite priorite,
        TypeIntervention typeIntervention,
        Long vehiculeId,
        Long mecanicienId
) {
}