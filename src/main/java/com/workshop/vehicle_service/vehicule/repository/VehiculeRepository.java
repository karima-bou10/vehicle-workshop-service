package com.workshop.vehicle_service.vehicule.repository;

import com.workshop.vehicle_service.vehicule.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeRepository  extends JpaRepository<Vehicule, Long> {
}
