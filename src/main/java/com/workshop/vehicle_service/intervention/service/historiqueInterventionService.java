package com.workshop.vehicle_service.intervention.service;

import com.workshop.vehicle_service.intervention.dtos.HistoriqueInterventionResponse;

import java.util.List;

public interface historiqueInterventionService {
    List<HistoriqueInterventionResponse> getHistoriqueIntervention(Long interventionId);
}
