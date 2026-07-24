package com.workshop.vehicle_service.mecanicien.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMecanicienRequest {
    private String nom;
    private String prenom;
    private String specialite;
}
