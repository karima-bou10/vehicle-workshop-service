package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.HistoriqueInterventionRepository;
import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.HistoriqueInterventionResponse;
import com.workshop.vehicle_service.intervention.entity.HistoriqueIntervention;
import com.workshop.vehicle_service.intervention.mapper.HistoriqueInterventionMapper;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.intervention.service.historiqueInterventionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class historiqueInterventionServiceImpl implements historiqueInterventionService {

    private final InterventionService interventionService;
    private final HistoriqueInterventionRepository historiqueInterventionRepository;
    private final HistoriqueInterventionMapper    historiqueInterventionMapper;

    @Override
    public List<HistoriqueInterventionResponse> getHistoriqueIntervention(Long interventionId) {

       interventionService.recupererUneIntervention(interventionId);

        List<HistoriqueIntervention> historiques =
                historiqueInterventionRepository
                        .findByInterventionIdOrderByDateModificationDesc(interventionId);

        return historiqueInterventionMapper.toResponseList(historiques);
    }
}
