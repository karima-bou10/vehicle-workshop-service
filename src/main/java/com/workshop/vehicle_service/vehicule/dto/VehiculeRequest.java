package com.workshop.vehicle_service.vehicule.dto;

public record VehiculeRequest(

        String immatriculationFictive,
        String marque,
        String modele,
        Integer annee,
        Integer kilometrage,
        String clientFictif

) {}
