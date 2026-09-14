package com.workshop.vehicle_service.vehicule.entity;

import com.workshop.vehicle_service.intervention.entity.Intervention;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "vehicule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "immatriculation_fictive", nullable = false, length = 20)
    private String immatriculationFictive;

    @Column(name = "marque", nullable = false, length = 50)
    private String marque;

    @Column(name = "modele", nullable = false, length = 50)
    private String modele;

    private Integer annee;
    private Integer kilometrage;

    @Column(name = "client_fictif", length =100)
    private String clientFictif;

    @OneToMany(mappedBy = "vehicule",fetch = FetchType.LAZY)
    List<Intervention> interventions;

}
