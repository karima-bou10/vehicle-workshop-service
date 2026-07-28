package com.workshop.vehicle_service.intervention.dtos;
import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InterventionRequest(

        Long id, @NotNull
        TypeIntervention typeIntervention,

        @NotBlank
        String descriptionClient,

        @NotBlank
        String diagnostic,

        @NotNull
        StatutIntervention statut,

        @NotNull
        Priorite priorite,

        @NotBlank
        BigDecimal coutEstime,

        @NotNull
        LocalDateTime dateDepot,

        LocalDateTime dateRestitutionPrevue,

        LocalDateTime dateCloture,

        @NotNull
        Long vehiculeId,

        @NotNull
        Long mecanicienId

) {
}