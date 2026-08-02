package com.workshop.vehicle_service.mecanicien.service;

import com.workshop.vehicle_service.common.ResourceNotFoundException;
import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.mecanicien.mapper.MecanicienMapper;
import com.workshop.vehicle_service.mecanicien.repository.MecanicienRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MecanicienServiceImp implements MecanicienService {

    private final MecanicienRepository mecanicienRepository;
    private final MecanicienMapper mecanicienMapper;

    private static final Logger logger = LogManager.getLogger(MecanicienServiceImp.class);

    @Override
    public Page<MecanicienResponse> getAllMecaniciens(Pageable pageable) {
        Page<Mecanicien> mecaniciensPage = mecanicienRepository.findAll(pageable);
        return mecaniciensPage.map(mecanicienMapper::toResponse);
    }

    @Override
    public MecanicienResponse getMecanicienById(Long id) {
        Mecanicien mecanicien = mecanicienRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Mécanicien introuvable avec l'id" + id));

        return mecanicienMapper.toResponse(mecanicien);
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
        if(!mecanicienRepository.existsById(id)){
            throw  new ResourceNotFoundException("Mécanicien introuvable avec l'id: "+id);
        };
        mecanicienRepository.deleteById(id);
        logger.info("Mecanicien deleted successfully");
    }

    @Override
    public Page<MecanicienResponse> getMecaniciensDisponibles(Pageable pageable) {
        Page<Mecanicien> mecaniciensPage = mecanicienRepository.findByDisponibleTrue(pageable);
        return mecaniciensPage.map(mecanicienMapper::toResponse);
    }

    @Override
    public Page<MecanicienResponse> searchMecaniciens(String keyword, Pageable pageable) {
        if(keyword == null || keyword.trim().isEmpty()){
            return getAllMecaniciens(pageable);
        }
        Page<Mecanicien> mecaniciensPage = mecanicienRepository.searchByKeyword(keyword, pageable);
        return mecaniciensPage.map(mecanicienMapper::toResponse);
    }
}
