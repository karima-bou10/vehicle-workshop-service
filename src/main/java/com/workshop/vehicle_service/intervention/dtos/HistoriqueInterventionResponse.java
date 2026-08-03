package com.workshop.vehicle_service.intervention.dtos;

import java.time.LocalDateTime;

public record HistoriqueInterventionResponse(
        Long id,

        String ancienStatut,

        String nouveauStatut,

        String commentaire,

        String auteur,

        LocalDateTime dateModification) {

}
