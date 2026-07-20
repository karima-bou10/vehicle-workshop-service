package com.workshop.vehicle_service.intervention.dtos;
import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InterventionRequest(

        Long id,

        TypeIntervention typeIntervention,

        String descriptionClient,

        String diagnostic,

        StatutIntervention statut,

        Priorite priorite,

        BigDecimal coutEstime,

        LocalDateTime dateDepot,

        LocalDateTime dateRestitutionPrevue,

        LocalDateTime dateCloture,

        Long vehiculeId,

        Long mecanicienId

) {
}