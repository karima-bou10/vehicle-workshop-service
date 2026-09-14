package com.workshop.vehicle_service.intervention.dtos;

import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InterventionCreationRequest(

        @NotNull
        TypeIntervention typeIntervention,

        @NotBlank
        String descriptionClient,

        @NotNull
        Priorite priorite,

        @NotNull
        LocalDateTime dateDepot,

        @NotNull
        LocalDateTime dateRestitutionPrevue,

        @NotNull
        Long vehiculeId

) {
}
