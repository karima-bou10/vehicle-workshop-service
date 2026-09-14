package com.workshop.vehicle_service.intervention.dtos;


import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record InterventionResponse(

        Long id,

        TypeIntervention typeIntervention,

        String descriptionClient,

        String diagnostic,

        StatutIntervention statut,

        Priorite priorite,

        BigDecimal coutEstime,

        LocalDateTime dateDepot,

        LocalDateTime dateRestitutionPrevue,

        LocalDateTime dateCloture,

        Long vehiculeId,

        String immatriculationVehicule,

        String marque,

        String modele,

        Long mecanicienId,

        String nomMecanicien,

        String prenomMecanicien,

        String reference,

        Boolean deleted
) {
}
