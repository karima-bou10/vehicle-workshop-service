package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.HistoriqueInterventionRepository;
import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.HistoriqueInterventionResponse;
import com.workshop.vehicle_service.intervention.entity.HistoriqueIntervention;
import com.workshop.vehicle_service.intervention.mapper.HistoriqueInterventionMapper;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.intervention.service.historiqueInterventionService;

import java.util.List;

public class historiqueInterventionServiceImpl implements historiqueInterventionService {

    private  InterventionRepository interventionRepository;
    private HistoriqueInterventionRepository historiqueInterventionRepository;
    private HistoriqueInterventionMapper    historiqueInterventionMapper;

    @Override
    public List<HistoriqueInterventionResponse> getHistoriqueIntervention(Long interventionId) {

        interventionRepository.findById(interventionId)
                .orElseThrow(() -> new RuntimeException("Intervention introuvable"));

        List<HistoriqueIntervention> historiques =
                historiqueInterventionRepository
                        .findByInterventionIdOrderByDateModificationDesc(interventionId);

        return historiqueInterventionMapper.toResponseList(historiques);
    }
}
