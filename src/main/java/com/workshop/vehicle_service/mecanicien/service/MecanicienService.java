package com.workshop.vehicle_service.mecanicien.service;

import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MecanicienService {
    Page<MecanicienResponse> getAllMecaniciens(Pageable pageable);

    MecanicienResponse getMecanicienById(Long id);

    MecanicienResponse saveMecanicien(CreateMecanicienRequest request);

    MecanicienResponse updateMecanicien(Long id, UpdateMecanicienRequest request);

    void deleteMecanicienById(Long id);

    Page<MecanicienResponse> getMecaniciensDisponibles(Pageable pageable);

    Page<MecanicienResponse> searchMecaniciens(String keyword, Pageable pageable);
}
