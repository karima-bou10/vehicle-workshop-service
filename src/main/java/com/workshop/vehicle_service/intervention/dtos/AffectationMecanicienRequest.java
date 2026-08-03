package com.workshop.vehicle_service.intervention.dtos;

import jakarta.validation.constraints.NotNull;

public record AffectationMecanicienRequest(

        @NotNull
        Long mecanicienId
) {
}
