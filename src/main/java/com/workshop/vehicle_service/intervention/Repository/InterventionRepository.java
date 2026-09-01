package com.workshop.vehicle_service.intervention.Repository;

import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long>, JpaSpecificationExecutor<Intervention> {
    List<Intervention> getInterventionByVehiculeId(Long vehiculeId);
    List<Intervention> getInterventionByVehiculeIdAndStatutIn(Long vehiculeId, List<StatutIntervention> statuts);

    long countByDateRestitutionPrevueBeforeAndStatutIsNotIn(LocalDateTime date, List<StatutIntervention> statut);
    List<Intervention> findByDateRestitutionPrevueBeforeAndStatutIsNot(LocalDateTime maintenant, StatutIntervention statut);

    List<Intervention> findByDeletedFalse();
    List<Intervention> findByDeletedTrue();
    List<Intervention> getInterventionByMecanicienId(Long mecanicienId);

    @Query("""
    SELECT i
    FROM Intervention i
    WHERE i.deleted = false
      AND i.dateRestitutionPrevue < CURRENT_TIMESTAMP
      AND i.statut NOT IN (
            com.workshop.vehicle_service.intervention.enums.StatutIntervention.RESTITUEE,
            com.workshop.vehicle_service.intervention.enums.StatutIntervention.ANNULEE
      )
""")
    List<Intervention> findInterventionsEnRetard();


}


