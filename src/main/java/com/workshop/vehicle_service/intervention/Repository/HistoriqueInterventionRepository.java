package com.workshop.vehicle_service.intervention.Repository;


import com.workshop.vehicle_service.intervention.entity.HistoriqueIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueInterventionRepository extends JpaRepository<HistoriqueIntervention, Long> {
}
