package com.workshop.vehicle_service.intervention.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DevisRequest (
        @NotNull
        BigDecimal coutEstime
){
}
