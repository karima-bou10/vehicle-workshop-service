package com.workshop.vehicle_service.mecanicien.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMecanicienRequest {
    private String nom;
    private String prenom;
    private String specialite;
    private Boolean disponible;
}
