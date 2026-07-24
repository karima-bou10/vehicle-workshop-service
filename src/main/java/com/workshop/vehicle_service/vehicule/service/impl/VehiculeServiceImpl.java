package com.workshop.vehicle_service.vehicule.service.impl;

import com.workshop.vehicle_service.common.ResourceNotFoundException;
import com.workshop.vehicle_service.vehicule.dto.VehiculeRequest;
import com.workshop.vehicle_service.vehicule.dto.VehiculeResponse;
import com.workshop.vehicle_service.vehicule.entity.Vehicule;
import com.workshop.vehicle_service.vehicule.mapper.VehiculeMapper;
import com.workshop.vehicle_service.vehicule.repository.VehiculeRepository;
import com.workshop.vehicle_service.vehicule.service.VehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;


    @Override
    public Page<VehiculeResponse> getAllVehicules(Pageable pageable) {
        return vehiculeRepository.findAll(pageable).map(vehiculeMapper::toResponse);
    }

    @Override
    public VehiculeResponse getVehiculeById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                                          .orElseThrow(()->new ResourceNotFoundException("Véhicule introuvable avec l'id" + id));

        return vehiculeMapper.toResponse(vehicule);
    }

    @Override
    public VehiculeResponse createVehicule(VehiculeRequest request) {
        Vehicule vehicule = vehiculeMapper.toEntity(request);
        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    @Override
    public VehiculeResponse updateVehicule(Long id, VehiculeRequest request) {
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Véhicule introuvable avec l'id"+id));
        vehiculeMapper.updateEntityFromRequest(request, vehicule);
        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    @Override
    public Void deleteVehicule(Long id) {
        if(!vehiculeRepository.existsById(id)){
            throw  new ResourceNotFoundException("Véhicule introuvable avec l'id"+id);
        };
        vehiculeRepository.deleteById(id);
        return null;
    }
}
