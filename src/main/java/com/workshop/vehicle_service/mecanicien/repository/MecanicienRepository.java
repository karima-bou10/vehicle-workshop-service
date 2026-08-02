package com.workshop.vehicle_service.mecanicien.repository;

import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MecanicienRepository extends JpaRepository<Mecanicien, Long> {

    //SQL: SELECT * FROM mecanicien WHERE disponible = true
    Page<Mecanicien> findByDisponibleTrue(Pageable pageable);

    @Query("SELECT m FROM Mecanicien m WHERE " +
            "LOWER(m.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.specialite) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Mecanicien> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
