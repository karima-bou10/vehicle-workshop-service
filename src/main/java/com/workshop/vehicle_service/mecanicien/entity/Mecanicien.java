package com.workshop.vehicle_service.mecanicien.entity;

import com.workshop.vehicle_service.intervention.entity.Intervention;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mecanicien")
public class Mecanicien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mecanicien")
    private Long id;

    @Column(name="nom", nullable = false)
    private String nom;

    @Column(name="prenom", nullable = false)
    private String prenom;

    @Column(name="specialite", nullable = false)
    private String specialite;

    @Column(name="disponible", nullable = false)
    private boolean disponible = true;

    @OneToMany(mappedBy = "mecanicien",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Intervention> inetventions;
}