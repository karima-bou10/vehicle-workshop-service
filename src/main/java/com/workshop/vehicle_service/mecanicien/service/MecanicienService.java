package com.workshop.vehicle_service.mecanicien.service;

import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MecanicienService {
    Page<MecanicienResponse> getAllMecaniciens(Pageable pageable);

    MecanicienResponse getMecanicienById(Long id);

    MecanicienResponse saveMecanicien(CreateMecanicienRequest request);

    MecanicienResponse updateMecanicien(Long id, UpdateMecanicienRequest request);

    void deleteMecanicienById(Long id);

    List<MecanicienResponse> getMecaniciensDisponibles();
}
