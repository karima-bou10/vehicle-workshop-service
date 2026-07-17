package com.workshop.vehicle_service.vehicule.service.impl;

import com.workshop.vehicle_service.vehicule.dto.VehiculeRequest;
import com.workshop.vehicle_service.vehicule.dto.VehiculeResponse;
import com.workshop.vehicle_service.vehicule.mapper.VehiculeMapper;
import com.workshop.vehicle_service.vehicule.repository.VehiculeRepository;
import com.workshop.vehicle_service.vehicule.service.VericuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VericuleService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;


    @Override
    public List<VehiculeResponse> findAll() {

        return List.of();
    }

    @Override
    public VehiculeResponse findById(Long id) {
        return null;
    }

    @Override
    public VehiculeResponse create(VehiculeRequest request) {
        return null;
    }

    @Override
    public VehiculeResponse update(Long id, VehiculeRequest request) {
        return null;
    }

    @Override
    public Void delete(Long id) {
        return null;
    }
}
