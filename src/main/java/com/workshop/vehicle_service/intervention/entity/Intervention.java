package com.workshop.vehicle_service.intervention.entity;

import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.vehicule.entity.Vehicule;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "intervention")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_intervention")
    private Long id;

    @Column(name="reference_intervention")
    private String référence;

    @Column(name="type_intervention", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeIntervention typeIntervention;

    @Column(name="description_client", nullable = false)
    private String descriptionClient ;

    @Column(name="diagnostic")
    private String diagnostic ;

    @Column(name="statut")
    @Enumerated(EnumType.STRING)
    private StatutIntervention statut;

    @Column(name="priorite")
    private Priorite  priorite;

    @Column(name="cout_estime")
    private BigDecimal coutEstime ;

    @Column(name="date_depot")
    private LocalDateTime dateDepot ;

    @Column(name="date_restitution_preuve", nullable = true)
    private  LocalDateTime  dateRestitutionPrevue;

    @Column(name="date_cloture", nullable = true)
    private LocalDateTime  dateCloture ;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "mecanicien_id", nullable = true)
    private Mecanicien mecanicien;

    @OneToMany(mappedBy = "intervention",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoriqueIntervention> historiqueInterventionList;

}
