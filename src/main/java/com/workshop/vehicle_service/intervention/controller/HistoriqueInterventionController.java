package com.workshop.vehicle_service.intervention.controller;


import com.workshop.vehicle_service.intervention.dtos.HistoriqueInterventionResponse;
import com.workshop.vehicle_service.intervention.service.historiqueInterventionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/intervention")
@AllArgsConstructor
public class HistoriqueInterventionController {

    private historiqueInterventionService historiqueInterventionService;

    @GetMapping("/{id}/historique")
    public List<HistoriqueInterventionResponse> getHistorique(
            @PathVariable Long id) {
        return historiqueInterventionService.getHistoriqueIntervention(id);
    }


}
