package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.HistoriqueInterventionRepository;
import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.ChangementStatutRequest;
import com.workshop.vehicle_service.intervention.entity.HistoriqueIntervention;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.service.StatutInterventionService;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.mecanicien.service.MecanicienService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatutInterventionServiceImpl implements StatutInterventionService {

    @Autowired
    private final HistoriqueInterventionRepository historiqueInterventionRepository;

    @Autowired
    private final MecanicienService mecanicienService;


    @Override
    public Intervention changerStatutIntervention(
           Intervention intervention,
            ChangementStatutRequest request) {



        verifierManager();


        StatutIntervention nouveauStatut = request.nouveauStatut();
        String commentaire = request.commentaire();

        verifierTransition(intervention, nouveauStatut);

        StatutIntervention ancienStatut = intervention.getStatut();

        verifierCout(intervention, nouveauStatut);

        verifierMecanicien(intervention, nouveauStatut);

        verifierRestitution(intervention, nouveauStatut);

        verifierAnnulation(intervention, nouveauStatut, commentaire);

        modifierStatut(intervention, nouveauStatut);

        creerHistoriqueIntervention(
                intervention,
                ancienStatut,
                nouveauStatut,
                commentaire
        );

        return intervention;
    }

    private void modifierStatut(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        intervention.setStatut(nouveauStatut);


        if (nouveauStatut == StatutIntervention.TERMINEE) {
            intervention.setDateCloture(LocalDateTime.now());
            mecanicienService.getMecanicienById(intervention.getMecanicien().getId()).setDisponible(true);
        }
    }

    private void creerHistoriqueIntervention(
            Intervention intervention,
            StatutIntervention ancienStatut,
            StatutIntervention nouveauStatut,
            String commentaire) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        HistoriqueIntervention historique = new HistoriqueIntervention();

        historique.setIntervention(intervention);
        historique.setAncienStatut(ancienStatut);
        historique.setNouveauStatut(nouveauStatut);
        historique.setCommentaire(commentaire);
        historique.setAuteur(authentication.getName());
        historique.setDateModification(LocalDateTime.now());

        historiqueInterventionRepository.save(historique);
    }

    private void verifierTransition(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        StatutIntervention actuel = intervention.getStatut();

        switch (actuel) {

            case RECUE -> {
                if (nouveauStatut != StatutIntervention.DIAGNOSTIC_EN_COURS
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite.");
            }

            case DIAGNOSTIC_EN_COURS -> {
                if (nouveauStatut != StatutIntervention.DEVIS_A_VALIDER
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite.");
            }

            case DEVIS_A_VALIDER -> {
                if (nouveauStatut != StatutIntervention.EN_REPARATION
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite.");
            }

            case EN_REPARATION -> {
                if (nouveauStatut != StatutIntervention.TERMINEE
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite.");
            }

            case TERMINEE -> {
                if (nouveauStatut != StatutIntervention.RESTITUEE)
                    throw new RuntimeException("Transition interdite.");
            }

            default ->
                    throw new RuntimeException("Aucune transition autorisée.");
        }
    }

    private void verifierMecanicien(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        if (nouveauStatut == StatutIntervention.EN_REPARATION) {

            if (intervention.getMecanicien() == null)
                throw new RuntimeException("Aucun mécanicien affecté.");
        }
    }

    private void verifierCout(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        if (nouveauStatut == StatutIntervention.DEVIS_A_VALIDER) {

            if (intervention.getCoutEstime() == null)
                throw new RuntimeException("Le coût estimé est obligatoire.");

            if (intervention.getCoutEstime().compareTo(BigDecimal.ZERO) <= 0)
                throw new RuntimeException("Le coût estimé doit être supérieur à zéro.");
        }
    }

    private void verifierAnnulation(
            Intervention intervention,
            StatutIntervention nouveauStatut,
            String commentaire) {

        if (nouveauStatut != StatutIntervention.ANNULEE)
            return;

        if (intervention.getStatut() == StatutIntervention.TERMINEE
                || intervention.getStatut() == StatutIntervention.RESTITUEE)
            throw new RuntimeException(
                    "Impossible d'annuler une intervention terminée ou restituée.");

        if (commentaire == null || commentaire.isBlank())
            throw new RuntimeException("Le commentaire est obligatoire.");
    }

    private void verifierRestitution(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        if (nouveauStatut != StatutIntervention.RESTITUEE)
            return;

        if (intervention.getStatut() != StatutIntervention.TERMINEE)
            throw new RuntimeException(
                    "L'intervention doit être terminée avant la restitution.");
    }

    private void verifierManager() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isManager = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));

        if (!isManager) {
            throw new RuntimeException(
                    "Seul un manager est autorisé à changer le statut.");
        }
    }
}