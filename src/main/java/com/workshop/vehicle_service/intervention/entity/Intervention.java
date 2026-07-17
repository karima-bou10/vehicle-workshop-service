package com.workshop.vehicle_service.intervention.entity;

import com.workshop.vehicle_service.Historique.entity.HistoriqueIntervention;
import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervension;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.vehicule.entity.Vehicule;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name = "intervention")
public class Intervention {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_intervention")
    private Long id;

    @Column(name="type_intervention", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeIntervension typeIntervention;

    @Column(name="description_client", nullable = false)
    private String descriptionClient ;

    @Column(name="diagnostic", nullable = false)
    private String diagnostic ;

    @Column(name="statut", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutIntervention statut;

    @Column(name="priorite", nullable = false)
    @Enumerated(EnumType.STRING)
    private Priorite  priorite;

    @Column(name="cout_Estime", nullable = false)
    private BigDecimal coutEstime ;

    @Column(name="date_depot", nullable = false)
    private LocalDateTime dateDepot ;

    @Column(name="date_restitution_preuve", nullable = false)
    private  LocalDateTime  dateRestitutionPrevue;

    @Column(name="date_cloture", nullable = false)
    private LocalDateTime  dateCloture ;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "intervention_id", nullable = false)
    private Mecanicien mecanicien;

    @OneToMany(mappedBy = "intervention",fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<HistoriqueIntervention> historiqueInterventionList;

}
