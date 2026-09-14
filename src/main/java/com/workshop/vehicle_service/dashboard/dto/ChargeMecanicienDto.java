package com.workshop.vehicle_service.dashboard.dto;

public record ChargeMecanicienDto(
        Long mecanicienId,
        String mecanicienNom,
        long nombreInterventionsActives
) {}
