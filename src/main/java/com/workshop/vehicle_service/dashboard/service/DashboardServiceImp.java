package com.workshop.vehicle_service.dashboard.service;

import com.workshop.vehicle_service.dashboard.dto.*;
import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.service.MecanicienService;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardServiceImp implements DashboardService {

    private final InterventionService interventionService;
    private final MecanicienService mecanicienService;
    private final InterventionRepository interventionRepository;

    @Override
    public DashboardResumeDto calculerResumeGlobal() {
        List<InterventionResponse> toutesLesInterventions = interventionService.recupererListInterventions();
        String dateDuJour = LocalDate.now().toString();

        long recuesAujourdhui = toutesLesInterventions.stream()
                .filter(i -> i.dateDepot() != null && i.dateDepot().toString().startsWith(dateDuJour))
                .count();

        long enDiagnostic = toutesLesInterventions.stream()
                .filter(i -> StatutIntervention.DIAGNOSTIC_EN_COURS.equals(i.statut()))
                .count();

        long enReparation = toutesLesInterventions.stream()
                .filter(i -> StatutIntervention.EN_REPARATION.equals(i.statut()))
                .count();

        long terminees = toutesLesInterventions.stream()
                .filter(i -> StatutIntervention.TERMINEE.equals(i.statut()))
                .count();

        long retardsRestitution = interventionRepository.countByDateRestitutionPrevueBeforeAndStatutIsNotIn(
                LocalDateTime.now(),List.of(StatutIntervention.RESTITUEE, StatutIntervention.ANNULEE));

        // Regroupement par statut
        Map<StatutIntervention, Long> compteurs = toutesLesInterventions.stream()
                .collect(Collectors.groupingBy(InterventionResponse::statut, Collectors.counting()));

        List<StatutCompteurDto> repartition = compteurs.entrySet().stream()
                .map(entry -> new StatutCompteurDto(entry.getKey().toString(), entry.getValue()))
                .toList();

        return new DashboardResumeDto(recuesAujourdhui, enDiagnostic, enReparation, terminees, retardsRestitution, repartition);
    }

    @Override
    public List<ChargeMecanicienDto> calculerChargeMecaniciens() {
        List<MecanicienResponse> mecaniciens = mecanicienService.getAllMecaniciens(PageRequest.of(0, 10)).getContent();
        List<InterventionResponse> interventions = interventionService.recupererListInterventions();

        return mecaniciens.stream().map(mec -> {
            long charge = interventions.stream()
                    .filter(i -> i.mecanicienId() != null && i.mecanicienId().equals(mec.getId()))
                    .filter(i -> !List.of("TERMINEE", "RESTITUEE", "ANNULEE").contains(i.statut().toString()))
                    .count();

            return new ChargeMecanicienDto(mec.getId(), mec.getNom(), charge);
        }).toList();
    }
}
