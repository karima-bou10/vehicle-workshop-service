package com.workshop.vehicle_service.vehicule.service.impl;

import com.workshop.vehicle_service.common.ResourceNotFoundException;
import com.workshop.vehicle_service.intervention.api.InterventionQuery;
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

/**
 * Implémentation du service de gestion des véhicules.
 */

@Service
@Transactional
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;

    private final InterventionQuery interventionQuery;

/**
     * Récupère la liste paginée de tous les véhicules.
     *
     * @param pageable les informations de pagination
     * @return une page d'objets VehiculeResponse représentant tous les véhicules
     */
    @Override
    public Page<VehiculeResponse> getAllVehicules(String search, Pageable pageable) {
        Page<Vehicule> result = (search != null && !search.isBlank())
                ?vehiculeRepository.searchByKeyword(search.trim(), pageable)
                :vehiculeRepository.findAll(pageable);
        return result.map(vehiculeMapper::toResponse);
    }

    /**
     * Récupère un véhicule spécifique par son identifiant.
     *
     * @param id l'identifiant du véhicule à récupérer
     * @return un objet VehiculeResponse représentant le véhicule trouvé
     * @throws ResourceNotFoundException si le véhicule avec l'identifiant spécifié n'est pas trouvé
     */
    @Override
    public VehiculeResponse getVehiculeById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                                          .orElseThrow(()->new ResourceNotFoundException("Véhicule introuvable avec l'id" + id));

        return vehiculeMapper.toResponse(vehicule);
    }

    /**
     * Crée un nouveau véhicule à partir des informations fournies dans la requête.
     *
     * @param request l'objet VehiculeRequest contenant les informations du véhicule à créer
     * @return un objet VehiculeResponse représentant le véhicule créé
     */
    @Override
    public VehiculeResponse createVehicule(VehiculeRequest request) {
        Vehicule vehicule = vehiculeMapper.toEntity(request);
        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    /**
     * Met à jour un véhicule existant avec les nouvelles informations fournies dans la requête.
     *
     * @param id      l'identifiant du véhicule à mettre à jour
     * @param request l'objet VehiculeRequest contenant les nouvelles informations du véhicule
     * @return un objet VehiculeResponse représentant le véhicule mis à jour
     * @throws ResourceNotFoundException si le véhicule avec l'identifiant spécifié n'est pas trouvé
     */
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
        }

        if (!interventionQuery.listInterventionsByVehiculeId(id).isEmpty()) {
            throw new RuntimeException("Suppression impossible : le véhicule est déjà associé à des interventions existantes.");
        }

        vehiculeRepository.deleteById(id);
        return null;
    }
}
