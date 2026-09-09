package com.workshop.vehicle_service.intervention.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DevisRequest (
        @NotNull
        BigDecimal coutEstime,
        @NotNull
        LocalDateTime dateRestitutionPrevue
){
}
