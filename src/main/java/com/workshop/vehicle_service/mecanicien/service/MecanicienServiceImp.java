package com.workshop.vehicle_service.mecanicien.service;

import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.mecanicien.mapper.MecanicienMapper;
import com.workshop.vehicle_service.mecanicien.repository.MecanicienRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MecanicienServiceImp implements MecanicienService {

    private final MecanicienRepository mecanicienRepository;
    private final MecanicienMapper mecanicienMapper;

    private static final Logger logger = LogManager.getLogger(MecanicienServiceImp.class);

    @Override
    public List<MecanicienResponse> getAllMecaniciens() {
        List<Mecanicien> mecaniciens = (List<Mecanicien>)mecanicienRepository.findAll();
        if (mecaniciens.isEmpty()) {
            return Collections.emptyList();
        }
        return mecanicienMapper.toResponseList(mecaniciens);
    }

    @Override
    public MecanicienResponse getMecanicienById(Long id) {
        return mecanicienRepository.findById(id)
                .map(mecanicienMapper::toResponse).orElse(null);
    }

    @Override
    public MecanicienResponse saveMecanicien(CreateMecanicienRequest request){
        logger.info("Entering saveMecanicien()");
        Mecanicien mecanicien = mecanicienMapper.toEntity(request);
        Mecanicien savedMecanicien = mecanicienRepository.save(mecanicien);
        logger.debug("Saved mecanicien: ", savedMecanicien);
        return mecanicienMapper.toResponse(savedMecanicien);
    }

    @Override
    public MecanicienResponse updateMecanicien(Long id, UpdateMecanicienRequest request){
        logger.info("Entering updateMecanicien()");

        Mecanicien mecanicienExistant = mecanicienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mécanicien avec l'ID " + id + " non trouvé"));

        mecanicienMapper.updateEntityFromRequest(request,mecanicienExistant);
        Mecanicien updatedMecanicien = mecanicienRepository.save(mecanicienExistant);
        logger.debug("Updated mecanicien: ", updatedMecanicien);
        return mecanicienMapper.toResponse(updatedMecanicien);
    }

    @Override
    public void deleteMecanicienById(Long id) {
        mecanicienRepository.deleteById(id);
        logger.info("Mecanicien deleted successfully");
    }

    @Override
    public List<MecanicienResponse> getMecaniciensDisponibles() {
        List<Mecanicien> mecaniciensDisponibles = (List<Mecanicien>)mecanicienRepository.findByDisponibleTrue();
        if (mecaniciensDisponibles.isEmpty()) {
            return Collections.emptyList();
        }
        return mecanicienMapper.toResponseList(mecaniciensDisponibles);
    }
}
