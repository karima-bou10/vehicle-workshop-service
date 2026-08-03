package com.workshop.vehicle_service.intervention.Repository;

import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    List<Intervention> getInterventionByVehiculeId(Long vehiculeId);
    List<Intervention> getInterventionByVehiculeIdAndStatutIn(Long vehiculeId, List<StatutIntervention> statuts);

    long countByDateRestitutionPrevueBeforeAndStatutIsNot(LocalDateTime date, StatutIntervention statut);
    List<Intervention> findByDateRestitutionPrevueBeforeAndStatutIsNot(LocalDateTime maintenant, StatutIntervention statut);}
