package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.InterventionRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.mapper.InterventionMapper;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.intervention.service.StatutInterventionService;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.mecanicien.repository.MecanicienRepository;
import com.workshop.vehicle_service.vehicule.entity.Vehicule;
import com.workshop.vehicle_service.vehicule.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class interventionServiceImpl implements InterventionService {


    private final InterventionRepository interventionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final MecanicienRepository mecanicienRepository;
    private final InterventionMapper interventionMapper;
    private final StatutInterventionService statutInterventionService;

    @Override
    public List<InterventionResponse> recupererListInterventions() {

        return interventionMapper.toResponseList(
                interventionRepository.findAll()
        );

    }

    @Override
    public InterventionResponse recupererUneIntervention(Long idIntervention) {


        Intervention intervention = interventionRepository.findById(idIntervention)
                .orElseThrow(() -> new RuntimeException("Intervention introuvable"));

        return interventionMapper.toResponse(intervention);

    }

    @Override
    public InterventionResponse modifierUneIntervention(InterventionRequest interventionRequest) {
        Intervention intervention = interventionRepository.findById(interventionRequest.id())
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));

       /* if(!intervention.getStatut().equals(interventionRequest.statut()){
            statutInterventionService.changerStatutIntervention(interventionRequest);
        }*/
        interventionMapper.updateEntity(interventionRequest, intervention);

        Vehicule vehicule = vehiculeRepository.findById(interventionRequest.vehiculeId())
                .orElseThrow(() ->
                        new RuntimeException("Vehicule introuvable"));

        Mecanicien mecanicien = mecanicienRepository.findById(interventionRequest.mecanicienId())
                .orElseThrow(() ->
                        new RuntimeException("Mecanicien introuvable"));

        intervention.setVehicule(vehicule);
        intervention.setMecanicien(mecanicien);

        intervention = interventionRepository.save(intervention);

        return interventionMapper.toResponse(intervention);
    }

    @Override
    public InterventionResponse enregistrerUneIntervention(InterventionRequest interventionRequest) {
        log.debug("Début du traitement pour enregistrer une intervention");

        Vehicule vehicule = vehiculeRepository.findById(interventionRequest.vehiculeId())
                .orElseThrow(() -> new RuntimeException("Vehicule introuvable"));

        Mecanicien mecanicien = mecanicienRepository.findById(interventionRequest.mecanicienId())
                .orElseThrow(() -> new RuntimeException("Mecanicien introuvable"));

        Intervention intervention = interventionMapper.toEntity(interventionRequest);

        intervention.setVehicule(vehicule);
        intervention.setMecanicien(mecanicien);

        return interventionMapper.toResponse(
                interventionRepository.save(intervention)
        );

    }
    @Override
    public InterventionResponse supprimerUneIntervention(Long idIntervention) {
        Intervention intervention = interventionRepository.findById(idIntervention)
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));

        InterventionResponse response =
                interventionMapper.toResponse(intervention);

        interventionRepository.delete(intervention);

        return response;
    }

}
