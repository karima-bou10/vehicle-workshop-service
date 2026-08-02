package com.workshop.vehicle_service.intervention.dtos;

import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InterventionUpdateRequest(

        TypeIntervention typeIntervention,

        String descriptionClient,

        Priorite priorite,

        LocalDateTime dateRestitutionPrevue,

        LocalDateTime dateCloture

) {
}