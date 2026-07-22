package com.workshop.vehicle_service.mecanicien.dto;

import lombok.*;

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
}
