package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.*;
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
    public InterventionResponse modifierUneIntervention(InterventionUpdateRequest interventionUpdateRequest, Long idIntervention) {
        Intervention intervention =
                interventionRepository.findById(idIntervention)
                        .orElseThrow(() ->
                                new RuntimeException("Intervention introuvable"));


        interventionMapper.updateEntity(interventionUpdateRequest, intervention);


        return interventionMapper.toResponse(
                interventionRepository.save(intervention)
        );
    }

    @Override
    public InterventionResponse enregistrerUneIntervention(
            InterventionCreationRequest interventionRequest) {

        log.debug("Début création intervention");

        Vehicule vehicule = vehiculeRepository.findById(
                interventionRequest.vehiculeId()
        ).orElseThrow(() ->
                new RuntimeException("Véhicule introuvable"));


        Intervention intervention = new Intervention();

        intervention.setTypeIntervention(
                interventionRequest.typeIntervention()
        );

        intervention.setDescriptionClient(
                interventionRequest.descriptionClient()
        );

        intervention.setPriorite(
                interventionRequest.priorite()
        );

        intervention.setDateDepot(
                interventionRequest.dateDepot()
        );

        intervention.setDateRestitutionPrevue(
                interventionRequest.dateDepot()
        );


        // Règle RG-AUTO-03 :
        // une nouvelle intervention commence toujours par RECUE
        intervention.setStatut(StatutIntervention.RECUE);


        // Relation obligatoire RG-AUTO-01
        intervention.setVehicule(vehicule);


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

    @Override
    public InterventionResponse affectationMecanicienIntervention(
            Long interventionId,
            AffectationMecanicienRequest request) throws BusinessException {


        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));


        Mecanicien mecanicien = mecanicienRepository.findById(request.mecanicienId())
                .orElseThrow(() ->
                        new RuntimeException("Mécanicien introuvable"));


        if (!mecanicien.isDisponible()) {
            throw new RuntimeException(
                    "Le mécanicien n'est pas disponible"
            );
        }

        if (
                (intervention.getStatut() == StatutIntervention.EN_REPARATION) ||
                        (intervention.getStatut() == StatutIntervention.TERMINEE) ||
                        (intervention.getStatut() == StatutIntervention.RESTITUEE) ||
                        (intervention.getStatut() == StatutIntervention.ANNULEE)
        ) {
            throw new BusinessException(
                    "Impossible d'affecter un mécanicien à cette intervention"
            );
        }


        intervention.setMecanicien(mecanicien);


        Intervention saved = interventionRepository.save(intervention);


        return interventionMapper.toResponse(saved);
    }

    @Override
    public InterventionResponse ajouterDiagnostic(Long interventionId, DiagnosticRequest request) {
        Intervention intervention =
                interventionRepository.findById(interventionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Intervention introuvable"
                                ));


        intervention.setDiagnostic(
                request.diagnostic()
        );


        statutInterventionService.changerStatutIntervention(
                intervention,
                StatutIntervention.DIAGNOSTIC_EN_COURS,
                "Diagnostic renseigné",
                "ROLE_USER"
        );


        return interventionMapper.toResponse(
                interventionRepository.save(intervention)
        );
    }

    @Override
    public InterventionResponse ajouterDevis(
            Long interventionId,
            DevisRequest request) {


        Intervention intervention =
                interventionRepository.findById(interventionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Intervention introuvable"
                                ));


        if(intervention.getDiagnostic() == null
                || intervention.getDiagnostic().isBlank()) {

            throw new RuntimeException(
                    "Le diagnostic est obligatoire avant le devis"
            );
        }


        intervention.setCoutEstime(
                request.coutEstime()
        );


        statutInterventionService.changerStatutIntervention(
                intervention,
                StatutIntervention.DEVIS_A_VALIDER,
                "Devis créé",
                "ROLE_USER"
        );


        return interventionMapper.toResponse(
                interventionRepository.save(intervention)
        );
    }
}
