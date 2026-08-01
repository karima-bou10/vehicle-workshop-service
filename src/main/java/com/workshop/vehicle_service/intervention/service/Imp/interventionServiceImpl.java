package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.api.InterventionQuery;
import com.workshop.vehicle_service.intervention.dtos.InterventionRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
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
public class interventionServiceImpl implements InterventionService, InterventionQuery {


    private final InterventionRepository interventionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final MecanicienRepository mecanicienRepository;
    private final InterventionMapper interventionMapper;
    private final StatutInterventionService statutInterventionService;

    /**
     * Récupère la liste de toutes les interventions.
     *
     * @return une liste d'objets InterventionResponse représentant toutes les interventions
     */
    @Override
    public List<InterventionResponse> recupererListInterventions() {

        return interventionMapper.toResponseList(
                interventionRepository.findAll()
        );

    }

    /**
     * Récupère une intervention spécifique par son identifiant.
     *
     * @param idIntervention l'identifiant de l'intervention à récupérer
     * @return un objet InterventionResponse représentant l'intervention trouvée
     * @throws RuntimeException si l'intervention n'est pas trouvée
     */
    @Override
    public InterventionResponse recupererUneIntervention(Long idIntervention) {


        Intervention intervention = interventionRepository.findById(idIntervention)
                .orElseThrow(() -> new RuntimeException("Intervention introuvable"));

        return interventionMapper.toResponse(intervention);

    }
    /**
     * Modifie une intervention existante.
     *
     * @param interventionRequest l'objet InterventionRequest contenant les nouvelles informations de l'intervention
     * @return un objet InterventionResponse représentant l'intervention modifiée
     * @throws RuntimeException si l'intervention, le véhicule ou le mécanicien n'est pas trouvé
     */

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

    /**
     * Enregistre une nouvelle intervention.
     *
     * @param interventionRequest l'objet InterventionRequest contenant les informations de la nouvelle intervention
     * @return un objet InterventionResponse représentant l'intervention enregistrée
     * @throws RuntimeException si le véhicule ou le mécanicien n'est pas trouvé
     */
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

    /**
     * Supprime une intervention existante par son identifiant.
     *
     * @param idIntervention l'identifiant de l'intervention à supprimer
     * @return un objet InterventionResponse représentant l'intervention supprimée
     * @throws RuntimeException si l'intervention n'est pas trouvée
     */
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

    /**
     * Récupère la liste des interventions par id vehicule
     * @param vehiculeId
     * @return une liste d'objets InterventionResponse représentant les interventions d un vehicule
     */
    @Override
    public List<InterventionResponse> listInterventionsByVehiculeId(Long vehiculeId) {
        return interventionMapper.toResponseList(
                interventionRepository.getInterventionByVehiculeId(vehiculeId)
        );
    }

    /**
     * Récupère toutes les interventions associées à un véhicule spécifique.
     *
     * @param vehiculeId l'identifiant du véhicule dont on souhaite récupérer les interventions
     * @return une liste d'interventions associées au véhicule spécifié
     */
    @Override
    public List<InterventionResponse> getInterventionsByVehiculeId(Long vehiculeId) {
        List<Intervention> interventions = interventionRepository.getInterventionByVehiculeId(vehiculeId);
        return interventionMapper.toResponseList(interventions);
    }

 /**
     * Récupère toutes les interventions ayant un statut spécifique pour un véhicule donné.
     *
     * @param idVehicule l'identifiant du véhicule dont on souhaite récupérer les interventions
     * @param statut     le statut des interventions à récupérer
     * @return une liste d'interventions correspondant au statut spécifié pour le véhicule donné
     */
    @Override
    public List<InterventionResponse> getInterventionsByStatut(Long idVehicule, StatutIntervention statut) {
        List<Intervention> interventions = interventionRepository
                .getInterventionByVehiculeIdAndStatutIn(idVehicule, List.of(statut));
        return interventionMapper.toResponseList(interventions);
    }
}
