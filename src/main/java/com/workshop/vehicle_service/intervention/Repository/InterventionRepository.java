package com.workshop.vehicle_service.intervention.Repository;

import com.workshop.vehicle_service.intervention.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
}
