package com.workshop.vehicle_service.vehicule.service;

import com.workshop.vehicle_service.vehicule.dto.VehiculeRequest;
import com.workshop.vehicle_service.vehicule.dto.VehiculeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehiculeService {

    Page<VehiculeResponse> getAllVehicules(String search, Pageable pageable);

    VehiculeResponse getVehiculeById(Long id);

    VehiculeResponse createVehicule(VehiculeRequest request);

    VehiculeResponse updateVehicule(Long id, VehiculeRequest request);

    Void deleteVehicule(Long id);

    List<VehiculeResponse> getVehiculesDisponiblesPourIntervention();
}
