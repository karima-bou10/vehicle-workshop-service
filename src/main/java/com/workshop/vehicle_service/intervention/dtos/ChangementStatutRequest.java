package com.workshop.vehicle_service.intervention.dtos;


import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangementStatutRequest(
        @NotNull(message = "le nouveau statut est obligatoire")
        StatutIntervention nouveauStatut,
        @NotNull(message = "le commentaire est obligatoire")
        String commentaire
    ) {
    }

