package com.workshop.vehicle_service.mecanicien.repository;

import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MecanicienRepository extends JpaRepository<Mecanicien, Long> {

    //SQL: SELECT * FROM mecanicien WHERE disponible = true
    List<Mecanicien> findByDisponibleTrue();
}
