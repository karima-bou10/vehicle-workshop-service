package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.InterventionRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.mapper.InterventionMapper;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class interventionServiceImp implements InterventionService {


    private final InterventionRepository interventionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final MecanicienRepository mecanicienRepository;
    private final InterventionMapper interventionMapper;

    @Override
    public List<InterventionResponse> recupererListInterventions() {

        return interventionMapper.toResponseList(
                interventionRepository.findAll()
        );

    }

    @Override
    public InterventionResponse recupererUneIntervention(int id) {
        return null;
    }

    @Override
    public InterventionResponse modifierUneIntervention(InterventionRequest interventionRequest) {
        return null;
    }

    @Override
    public InterventionResponse EnregistrerUneIntervention(InterventionRequest interventionRequest) {
        log.debug("Début du traitement pour enregistrer une intervention");
         return null;
    }
    @Override
    public InterventionResponse supprimerUneIntervention(InterventionRequest interventionRequest) {
        return null;
    }

}
