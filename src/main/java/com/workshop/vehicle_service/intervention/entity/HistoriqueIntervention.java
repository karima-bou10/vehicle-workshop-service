package com.workshop.vehicle_service.intervention.entity;

import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "historique_intervention")
public class HistoriqueIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_intervention")
    private Long id;

    @Column(name = "ancien_statut", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutIntervention ancienStatut;

    @Column(name = "nouveau_statut", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutIntervention nouveauStatut;

    @Column(name = "commentaire", nullable = false)
    private String commentaire;

    @Column(name = "auteur", nullable = false)
    private String auteur;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "intervention_id")
    private Intervention intervention;

}
