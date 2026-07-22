package com.workshop.vehicle_service.intervention.service.Imp;

import com.workshop.vehicle_service.intervention.Repository.HistoriqueInterventionRepository;
import com.workshop.vehicle_service.intervention.entity.HistoriqueIntervention;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.service.StatutInterventionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class StatutInterventionServiceImpl implements StatutInterventionService {

    private final HistoriqueInterventionRepository historiqueInterventionRepository;

    @Override
    public Intervention changerStatutIntervention(Intervention intervention, StatutIntervention nouveauStatut, String commentaire, String roleUtilisateur) {
        verifierTransition(intervention, nouveauStatut);

        StatutIntervention ancienStatut = intervention.getStatut();

        verifierCout(intervention, nouveauStatut);

        verifierMecanicien(intervention, nouveauStatut);

        verifierRestitution(intervention, nouveauStatut, roleUtilisateur);

        verifierAnnulation(intervention, nouveauStatut, commentaire, roleUtilisateur);

        modifierStatut(nouveauStatut, intervention);

        creerHistoriqueIntervention(intervention,ancienStatut,nouveauStatut,commentaire,roleUtilisateur);

        return intervention;
    }

    private void creerHistoriqueIntervention(Intervention intervention, StatutIntervention
            ancienStatut, StatutIntervention nouveauStatut, String commentaire, String roleUtilisateur) {
        HistoriqueIntervention historique = new HistoriqueIntervention();
        historique.setIntervention(intervention);
        historique.setAncienStatut(ancienStatut);
        historique.setNouveauStatut(nouveauStatut);
        historique.setCommentaire(commentaire);
        historique.setAuteur(roleUtilisateur);
        historique.setDate(LocalDateTime.now());
        historiqueInterventionRepository.save(historique);
    }

    private void modifierStatut(StatutIntervention nouveauStatut, Intervention intervention) {
        intervention.setStatut(nouveauStatut);
    }

    private void verifierTransition(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        StatutIntervention actuel = intervention.getStatut();

        switch (actuel) {

            case RECUE -> {
                if (nouveauStatut != StatutIntervention.DIAGNOSTIC_EN_COURS
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite");
            }

            case DIAGNOSTIC_EN_COURS -> {
                if (nouveauStatut != StatutIntervention.DEVIS_A_VALIDER
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite");
            }

            case DEVIS_A_VALIDER -> {
                if (nouveauStatut != StatutIntervention.EN_REPARATION
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite");
            }

            case EN_REPARATION -> {
                if (nouveauStatut != StatutIntervention.TERMINEE
                        && nouveauStatut != StatutIntervention.ANNULEE)
                    throw new RuntimeException("Transition interdite");
            }

            case TERMINEE -> {
                if (nouveauStatut != StatutIntervention.RESTITUEE)
                    throw new RuntimeException("Transition interdite");
            }

            default -> throw new RuntimeException("Impossible");
        }
    }

    private void verifierMecanicien(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        if (nouveauStatut == StatutIntervention.EN_REPARATION
                && intervention.getMecanicien() == null) {

            throw new RuntimeException(
                    "Aucun mécanicien affecté");
        }
        if(!intervention.getMecanicien().isDisponible()){
            throw new RuntimeException("Le mecanicien n'est pas disponible");
        }
    }

    private void verifierCout(
            Intervention intervention,
            StatutIntervention nouveauStatut) {

        if (nouveauStatut == StatutIntervention.DEVIS_A_VALIDER
                && intervention.getCoutEstime() == null) {

            throw new RuntimeException(
                    "Le coût estimé est obligatoire");
        }
    }

    private void verifierAnnulation(
            Intervention intervention,
            StatutIntervention nouveauStatut,
            String commentaire,
            String role) {

        if (nouveauStatut == StatutIntervention.ANNULEE) {

            if (intervention.getStatut() == StatutIntervention.TERMINEE
                    || intervention.getStatut() == StatutIntervention.RESTITUEE)
                throw new RuntimeException("Impossible");

            if (commentaire == null || commentaire.isBlank())
                throw new RuntimeException("Commentaire obligatoire");

            if (!role.equals("ROLE_MANAGER"))
                throw new RuntimeException("Accès refusé");
        }
    }

    private void verifierRestitution(
            Intervention intervention,
            StatutIntervention nouveauStatut,
            String role) {

        if (nouveauStatut == StatutIntervention.RESTITUEE) {

            if (intervention.getStatut() != StatutIntervention.TERMINEE)
                throw new RuntimeException("Statut invalide");

            if (!role.equals("ROLE_MANAGER"))
                throw new RuntimeException("Accès refusé");
        }
    }
}
