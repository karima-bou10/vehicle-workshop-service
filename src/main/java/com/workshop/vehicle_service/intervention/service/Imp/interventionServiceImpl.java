package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.*;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.mapper.InterventionMapper;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.intervention.service.StatutInterventionService;
import com.workshop.vehicle_service.intervention.specification.InterventionSpecification;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.mecanicien.repository.MecanicienRepository;
import com.workshop.vehicle_service.vehicule.entity.Vehicule;
import com.workshop.vehicle_service.vehicule.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
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

    /**
     * Récupère la liste de toutes les interventions.
     *
     * @return une liste d'objets InterventionResponse représentant toutes les interventions
     */
    @Override
    public Page<InterventionResponse> recupererListInterventions(Pageable pageable) {

        return interventionRepository
                .findByDeletedFalse(pageable)
                .map(interventionMapper::toResponse);
    }


    @Override
    public List<InterventionResponse> recupererListInterventions() {

        List<Intervention> interventions =

                interventionRepository.findByDeletedFalse();

        return interventions.stream()

                .map(interventionMapper::toResponse)

                .toList();

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
     * @param "é"é&interventionUpdateRequest l'objet InterventionRequest contenant les nouvelles informations de l'intervention
     * @return un objet InterventionResponse représentant l'intervention modifiée
     * @throws RuntimeException si l'intervention, le véhicule ou le mécanicien n'est pas trouvé
     */

    @Override
    public InterventionResponse modifierUneIntervention(
            InterventionUpdateRequest request,
            Long idIntervention) throws BusinessException {

        Intervention intervention = interventionRepository.findById(idIntervention)
                .orElseThrow(() -> new RuntimeException("Intervention introuvable"));
        // Vérification du statut
        if (intervention.getStatut() != StatutIntervention.RECUE
                && intervention.getStatut() != StatutIntervention.DIAGNOSTIC_EN_COURS) {
            throw new BusinessException(
                    "La modification n'est autorisée que pour les interventions en statut RECUE ou DIAGNOSTIC_EN_COURS.");
        }

        interventionMapper.updateEntity(request, intervention);

        intervention = interventionRepository.save(intervention);

        return interventionMapper.toResponse(intervention);
    }

    @Override
    public InterventionResponse enregistrerUneIntervention(
            InterventionCreationRequest interventionRequest) {

        log.debug("Début création intervention");

        Vehicule vehicule = vehiculeRepository.findById(
                interventionRequest.vehiculeId()
        ).orElseThrow(() ->
                new RuntimeException("Véhicule introuvable"));

        List<StatutIntervention> statutsFinaux = List.of(
                StatutIntervention.TERMINEE,
                StatutIntervention.ANNULEE,
                StatutIntervention.RESTITUEE
        );

        boolean interventionActive =
                interventionRepository.existsByVehiculeIdAndDeletedFalseAndStatutNotIn(
                        vehicule.getId(),
                        statutsFinaux
                );

        if (interventionActive) {
            throw new BusinessException(
                    "Ce véhicule possède déjà une intervention en cours"
            );
        }

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
                interventionRequest.dateRestitutionPrevue()
        );

        // RG-AUTO-03
        intervention.setStatut(StatutIntervention.RECUE);

        // RG-AUTO-01
        intervention.setVehicule(vehicule);

        // Premier save pour générer l'id
        intervention = interventionRepository.save(intervention);

        // Génération de la référence
        intervention.setReference(
                "INT-" +
                        Year.now().getValue() +
                        "-" +
                        String.format("%05d", intervention.getId())
        );

        // Mise à jour avec la référence
        intervention = interventionRepository.save(intervention);

        return interventionMapper.toResponse(intervention);
    }
    @Override
    public InterventionResponse archiveUneIntervention(Long idIntervention) {

        Intervention intervention = interventionRepository.findById(idIntervention)
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));

        StatutIntervention statut = intervention.getStatut();

        if (statut != StatutIntervention.TERMINEE
                && statut != StatutIntervention.RESTITUEE
                && statut != StatutIntervention.ANNULEE) {

            throw new BusinessException(
                    "Impossible d'archiver une intervention à ce stade"
            );
        }

        intervention.setDeleted(true);

        Intervention interventionArchivee =
                interventionRepository.save(intervention);

        return interventionMapper.toResponse(interventionArchivee);
    }

    @Override
    public InterventionResponse affectationMecanicienIntervention(
            Long interventionId,
            AffectationMecanicienRequest request) throws BusinessException {

        // 1. Récupérer l'intervention
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));

        // 2. Vérifier le statut de l'intervention
        if (
                intervention.getStatut() == StatutIntervention.EN_REPARATION ||
                        intervention.getStatut() == StatutIntervention.TERMINEE ||
                        intervention.getStatut() == StatutIntervention.RESTITUEE ||
                        intervention.getStatut() == StatutIntervention.ANNULEE
        ) {
            throw new BusinessException(
                    "Impossible de modifier l'affectation à ce stade de l'intervention"
            );
        }

        // 3. Récupérer le nouveau mécanicien
        Mecanicien nouveauMecanicien = mecanicienRepository
                .findById(request.mecanicienId())
                .orElseThrow(() ->
                        new RuntimeException("Mécanicien introuvable"));

        // 4. Vérifier la disponibilité du nouveau mécanicien
        // Si c'est le même mécanicien déjà affecté, il est accepté
        boolean memeMecanicien =
                intervention.getMecanicien() != null &&
                        intervention.getMecanicien()
                                .getId()
                                .equals(nouveauMecanicien.getId());

        if (!memeMecanicien && !nouveauMecanicien.isDisponible()) {
            throw new BusinessException(
                    "Le mécanicien n'est pas disponible"
            );
        }

        // 5. Si un ancien mécanicien est affecté et qu'on le change,
        //    rendre l'ancien mécanicien disponible
        if (intervention.getMecanicien() != null && !memeMecanicien) {

            Mecanicien ancienMecanicien = intervention.getMecanicien();

            ancienMecanicien.setDisponible(true);
            mecanicienRepository.save(ancienMecanicien);
        }

        // 6. Affecter le nouveau mécanicien
        intervention.setMecanicien(nouveauMecanicien);

        // 7. Le nouveau mécanicien devient indisponible
        if (!memeMecanicien) {
            nouveauMecanicien.setDisponible(false);
            mecanicienRepository.save(nouveauMecanicien);
        }

        // 8. Sauvegarder l'intervention
        Intervention saved = interventionRepository.save(intervention);

        // 9. Retourner la réponse
        return interventionMapper.toResponse(saved);
    }

    @Override
    public InterventionResponse ajouterDiagnostic(Long interventionId, DiagnosticRequest request) throws BusinessException {

        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));

        // Si l'intervention est reçue, on ajoute le diagnostic
        // puis on passe automatiquement au statut DIAGNOSTIC_EN_COURS
        if (intervention.getStatut() == StatutIntervention.RECUE) {

            intervention.setDiagnostic(request.diagnostic());

            ChangementStatutRequest changementStatutRequest =
                    new ChangementStatutRequest(
                            StatutIntervention.DIAGNOSTIC_EN_COURS,
                            "Diagnostic renseigné"
                    );

            statutInterventionService.changerStatutIntervention(
                    intervention,
                    changementStatutRequest
            );

        }
        // Si le diagnostic est déjà en cours,
        // on autorise simplement sa modification
        else if (intervention.getStatut() == StatutIntervention.DIAGNOSTIC_EN_COURS) {

            intervention.setDiagnostic(request.diagnostic());

        }
        // Tous les autres statuts sont interdits
        else {
            throw new BusinessException(
                    "Le diagnostic ne peut plus être ajouté ou modifié."
            );
        }

        intervention = interventionRepository.save(intervention);

        return interventionMapper.toResponse(intervention);
    }

    @Override
    public InterventionResponse ajouterDevis(
            Long interventionId,
            DevisRequest request) {

        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));

        if (intervention.getDiagnostic() == null
                || intervention.getDiagnostic().isBlank()) {
            throw new RuntimeException(
                    "Le diagnostic est obligatoire avant le devis");
        }

        // Devis déjà existant
        if (intervention.getCoutEstime() != null) {

            // Autoriser uniquement la modification d'un devis en attente
            if (intervention.getStatut() != StatutIntervention.DEVIS_A_VALIDER) {
                throw new RuntimeException(
                        "Le devis ne peut plus être modifié");
            }

            intervention.setCoutEstime(request.coutEstime());

            return interventionMapper.toResponse(
                    interventionRepository.save(intervention));
        }

        // Création du devis
        intervention.setCoutEstime(request.coutEstime());

        statutInterventionService.changerStatutIntervention(
                intervention,
                new ChangementStatutRequest(
                        StatutIntervention.DEVIS_A_VALIDER,
                        "Devis créé"
                )
        );

        return interventionMapper.toResponse(
                interventionRepository.save(intervention));
    }

    @Override
    public InterventionResponse changerStatut(
            Long id,
            ChangementStatutRequest request) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention introuvable"));

        intervention = statutInterventionService.changerStatutIntervention(
                intervention,
                request
        );

        intervention = interventionRepository.save(intervention);

        return interventionMapper.toResponse(intervention);
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
     * Récupère la liste des interventions par id mecanicien
     * @param mecanicienId
     * @return une liste d'objets InterventionResponse représentant les interventions d'un mecanicien
     */
    @Override
    public List<InterventionResponse> listInterventionsByMecanicienId(Long mecanicienId) {
        return interventionMapper.toResponseList(
                interventionRepository.getInterventionByMecanicienId(mecanicienId)
        );
    }

    @Override
    public Page<InterventionResponse> recupererHistoriqueComplet(Pageable pageable) {

    return interventionRepository.findAll(pageable).map(interventionMapper::toResponse);

    }

    public Page<InterventionResponse> getInterventionsRestitueEnRetard(Pageable pageable) {

       return
                interventionRepository.findInterventionsEnRetard(pageable)
                .map(interventionMapper::toResponse);

    }
    @Override
    public Page<InterventionResponse> rechercherInterventions(
            InterventionSearchRequest request,
            Pageable pageable,boolean includeArchived) {

        System.out.println("REFERENCE REÇUE = [" + request.reference() + "]");

        var specification = InterventionSpecification.withFilters(
                request.reference(),
                request.immatriculation(),
                request.statut(),
                request.priorite(),
                request.typeIntervention(),
                request.vehiculeId(),
                request.mecanicienId(),
                includeArchived
        );

        return interventionRepository
                .findAll(specification, pageable)
                .map(interventionMapper::toResponse);
    }
}
