package com.workshop.vehicle_service.mecanicien.dto;

import com.workshop.vehicle_service.intervention.entity.Intervention;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MecanicienResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String specialite;
    private boolean disponible;
    private List<Intervention> interventions;
}
