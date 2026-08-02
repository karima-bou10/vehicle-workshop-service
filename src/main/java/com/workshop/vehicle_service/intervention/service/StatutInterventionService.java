package com.workshop.vehicle_service.intervention.service;

import com.workshop.vehicle_service.intervention.dtos.ChangementStatutRequest;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;

public interface StatutInterventionService {
    Intervention changerStatutIntervention(
            Intervention intervention,
            ChangementStatutRequest request);
}
