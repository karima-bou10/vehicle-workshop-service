package com.workshop.vehicle_service.vehicule.service;

import com.workshop.vehicle_service.vehicule.dto.VehiculeRequest;
import com.workshop.vehicle_service.vehicule.dto.VehiculeResponse;

import java.util.List;

public interface VericuleService {

    List<VehiculeResponse> findAll();

    VehiculeResponse findById(Long id);

    VehiculeResponse create(VehiculeRequest request);

    VehiculeResponse update(Long id, VehiculeRequest request);

    Void delete(Long id);

}
