package com.workshop.vehicle_service.vehicule.service.impl;

import com.workshop.vehicle_service.common.ResourceNotFoundException;
import com.workshop.vehicle_service.intervention.api.InterventionQuery;
import com.workshop.vehicle_service.vehicule.dto.VehiculeRequest;
import com.workshop.vehicle_service.vehicule.dto.VehiculeResponse;
import com.workshop.vehicle_service.vehicule.entity.Vehicule;
import com.workshop.vehicle_service.vehicule.mapper.VehiculeMapper;
import com.workshop.vehicle_service.vehicule.repository.VehiculeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires de VehiculeServiceImpl")
class VehiculeServiceImplTest {

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private VehiculeMapper vehiculeMapper;

    @Mock
    private InterventionQuery interventionQuery;

    @InjectMocks
    private VehiculeServiceImpl vehiculeService;

    private Vehicule vehicule;
    private VehiculeRequest vehiculeRequest;
    private VehiculeResponse vehiculeResponse;
    private Long vehiculeId;

    @BeforeEach
    void setUp() {
        vehiculeId = 1L;

        // Création d'une entité Vehicule
        vehicule = new Vehicule();
        vehicule.setId(vehiculeId);
        vehicule.setImmatriculationFictive("ABC-123");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Corolla");
        vehicule.setAnnee(2022);
        vehicule.setKilometrage(15000);
        vehicule.setClientFictif("Client Fictif");

        // Création d'une requête
        vehiculeRequest = new VehiculeRequest(
                "XYZ-789",
                "Honda",
                "Civic",
                2023,
                10000,
                "Nouveau Client"
        );

        // Création d'une réponse
        vehiculeResponse = new VehiculeResponse(
                vehiculeId,
                "ABC-123",
                "Toyota",
                "Corolla",
                2022,
                15000,
                "Client Fictif"
        );
    }

    // ==================== Tests pour getAllVehicules ====================

    @Test
    @DisplayName("Récupérer tous les véhicules - Success")
    void testGetAllVehicules_Success() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicule> vehiculePage = new PageImpl<>(List.of(vehicule), pageable, 1);

        when(vehiculeRepository.findAll(pageable)).thenReturn(vehiculePage);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(vehiculeResponse);

        
        Page<VehiculeResponse> result = vehiculeService.getAllVehicules("", pageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(vehiculeResponse, result.getContent().getFirst());
        verify(vehiculeRepository, times(1)).findAll(pageable);
        verify(vehiculeMapper, times(1)).toResponse(vehicule);
    }

    @Test
    @DisplayName("Récupérer tous les véhicules - Liste vide")
    void testGetAllVehicules_EmptyList() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicule> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(vehiculeRepository.findAll(pageable)).thenReturn(emptyPage);

        
        Page<VehiculeResponse> result = vehiculeService.getAllVehicules("", pageable);

        
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(vehiculeRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Récupérer tous les véhicules - Pagination")
    void testGetAllVehicules_WithPagination() {
        
        Vehicule vehicule2 = new Vehicule();
        vehicule2.setId(2L);
        vehicule2.setImmatriculationFictive("XYZ-456");

        VehiculeResponse vehiculeResponse2 = new VehiculeResponse(2L, "XYZ-456", "Honda", "Civic", 2023, 10000, "Client 2");

        Pageable pageable = PageRequest.of(0, 2);
        Page<Vehicule> vehiculePage = new PageImpl<>(List.of(vehicule, vehicule2), pageable, 2);

        when(vehiculeRepository.findAll(pageable)).thenReturn(vehiculePage);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(vehiculeResponse);
        when(vehiculeMapper.toResponse(vehicule2)).thenReturn(vehiculeResponse2);

        
        Page<VehiculeResponse> result = vehiculeService.getAllVehicules("", pageable);

        
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(vehiculeRepository, times(1)).findAll(pageable);
    }

    // ==================== Tests pour getVehiculeById ====================

    @Test
    @DisplayName("Récupérer un véhicule par ID - Success")
    void testGetVehiculeById_Success() {
        
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(vehiculeResponse);

        
        VehiculeResponse result = vehiculeService.getVehiculeById(vehiculeId);

        
        assertNotNull(result);
        assertEquals(vehiculeResponse, result);
        verify(vehiculeRepository, times(1)).findById(vehiculeId);
        verify(vehiculeMapper, times(1)).toResponse(vehicule);
    }

    @Test
    @DisplayName("Récupérer un véhicule par ID - Non trouvé")
    void testGetVehiculeById_NotFound() {
        
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.getVehiculeById(vehiculeId));
        verify(vehiculeRepository, times(1)).findById(vehiculeId);
        verify(vehiculeMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Récupérer un véhicule par ID - ID invalide")
    void testGetVehiculeById_WithInvalidId() {
        
        Long invalidId = -1L;
        when(vehiculeRepository.findById(invalidId)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.getVehiculeById(invalidId));
        verify(vehiculeRepository, times(1)).findById(invalidId);
    }

    // ==================== Tests pour createVehicule ====================

    @Test
    @DisplayName("Créer un nouveau véhicule - Success")
    void testCreateVehicule_Success() {
        
        when(vehiculeMapper.toEntity(vehiculeRequest)).thenReturn(vehicule);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(vehiculeResponse);

        
        VehiculeResponse result = vehiculeService.createVehicule(vehiculeRequest);

        
        assertNotNull(result);
        assertEquals(vehiculeResponse, result);
        verify(vehiculeMapper, times(1)).toEntity(vehiculeRequest);
        verify(vehiculeRepository, times(1)).save(vehicule);
        verify(vehiculeMapper, times(1)).toResponse(vehicule);
    }

    @Test
    @DisplayName("Créer un nouveau véhicule - Vérifier que save est appelé")
    void testCreateVehicule_VerifySave() {
        
        Vehicule newVehicule = new Vehicule();
        newVehicule.setId(2L);
        newVehicule.setImmatriculationFictive("NEW-111");

        VehiculeResponse newResponse = new VehiculeResponse(2L, "NEW-111", "BMW", "X5", 2024, 5000, "New Client");

        when(vehiculeMapper.toEntity(vehiculeRequest)).thenReturn(newVehicule);
        when(vehiculeRepository.save(newVehicule)).thenReturn(newVehicule);
        when(vehiculeMapper.toResponse(newVehicule)).thenReturn(newResponse);

        
        VehiculeResponse result = vehiculeService.createVehicule(vehiculeRequest);

        
        assertEquals(newResponse, result);
        verify(vehiculeRepository, times(1)).save(newVehicule);
    }

    // ==================== Tests pour updateVehicule ====================

    @Test
    @DisplayName("Mettre à jour un véhicule - Success")
    void testUpdateVehicule_Success() {
        
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        doNothing().when(vehiculeMapper).updateEntityFromRequest(vehiculeRequest, vehicule);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(vehiculeResponse);

        
        VehiculeResponse result = vehiculeService.updateVehicule(vehiculeId, vehiculeRequest);

        
        assertNotNull(result);
        assertEquals(vehiculeResponse, result);
        verify(vehiculeRepository, times(1)).findById(vehiculeId);
        verify(vehiculeMapper, times(1)).updateEntityFromRequest(vehiculeRequest, vehicule);
        verify(vehiculeRepository, times(1)).save(vehicule);
        verify(vehiculeMapper, times(1)).toResponse(vehicule);
    }

    @Test
    @DisplayName("Mettre à jour un véhicule - Non trouvé")
    void testUpdateVehicule_NotFound() {
        
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

         
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.updateVehicule(vehiculeId, vehiculeRequest));
        verify(vehiculeRepository, times(1)).findById(vehiculeId);
        verify(vehiculeMapper, never()).updateEntityFromRequest(any(), any());
        verify(vehiculeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Mettre à jour un véhicule - ID invalide")
    void testUpdateVehicule_WithInvalidId() {
        
        Long invalidId = -1L;
        when(vehiculeRepository.findById(invalidId)).thenReturn(Optional.empty());

         
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.updateVehicule(invalidId, vehiculeRequest));
        verify(vehiculeRepository, times(1)).findById(invalidId);
        verify(vehiculeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Mettre à jour un véhicule - Vérifier l'ordre d'appel des méthodes")
    void testUpdateVehicule_VerifyCallOrder() {
        
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        doNothing().when(vehiculeMapper).updateEntityFromRequest(vehiculeRequest, vehicule);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(vehiculeResponse);

        
        vehiculeService.updateVehicule(vehiculeId, vehiculeRequest);

        InOrder inOrder = inOrder(vehiculeRepository, vehiculeMapper);
        inOrder.verify(vehiculeRepository).findById(vehiculeId);
        inOrder.verify(vehiculeMapper).updateEntityFromRequest(vehiculeRequest, vehicule);
        inOrder.verify(vehiculeRepository).save(vehicule);
        inOrder.verify(vehiculeMapper).toResponse(vehicule);
    }

    // ==================== Tests pour deleteVehicule ====================

    @Test
    @DisplayName("Supprimer un véhicule - Success")
    void testDeleteVehicule_Success() {
        
        when(vehiculeRepository.existsById(vehiculeId)).thenReturn(true);
        when(interventionQuery.listInterventionsByVehiculeId(vehiculeId)).thenReturn(List.of());
        doNothing().when(vehiculeRepository).deleteById(vehiculeId);

        
        Void result = vehiculeService.deleteVehicule(vehiculeId);

        
        assertNull(result);
        verify(vehiculeRepository, times(1)).existsById(vehiculeId);
        verify(interventionQuery, times(1)).listInterventionsByVehiculeId(vehiculeId);
        verify(vehiculeRepository, times(1)).deleteById(vehiculeId);
    }

    @Test
    @DisplayName("Supprimer un véhicule - Non trouvé")
    void testDeleteVehicule_NotFound() {
        
        when(vehiculeRepository.existsById(vehiculeId)).thenReturn(false);

         
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.deleteVehicule(vehiculeId));
        verify(vehiculeRepository, times(1)).existsById(vehiculeId);
        verify(vehiculeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Supprimer un véhicule - ID invalide")
    void testDeleteVehicule_WithInvalidId() {
        
        Long invalidId = -1L;
        when(vehiculeRepository.existsById(invalidId)).thenReturn(false);

         
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.deleteVehicule(invalidId));
        verify(vehiculeRepository, times(1)).existsById(invalidId);
        verify(vehiculeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Supprimer un véhicule - Vérifier que deleteById est appelé")
    void testDeleteVehicule_VerifyDelete() {
        
        Long idToDelete = 5L;
        when(vehiculeRepository.existsById(idToDelete)).thenReturn(true);
        when(interventionQuery.listInterventionsByVehiculeId(idToDelete)).thenReturn(List.of());
        doNothing().when(vehiculeRepository).deleteById(idToDelete);

        
        vehiculeService.deleteVehicule(idToDelete);

        
        verify(interventionQuery, times(1)).listInterventionsByVehiculeId(idToDelete);
        verify(vehiculeRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    @DisplayName("Supprimer un véhicule - Impossible si des interventions existent")
    void testDeleteVehicule_WithExistingInterventions() {

        when(vehiculeRepository.existsById(vehiculeId)).thenReturn(true);
        when(interventionQuery.listInterventionsByVehiculeId(vehiculeId))
                .thenReturn(List.of(mock(com.workshop.vehicle_service.intervention.dtos.InterventionResponse.class)));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> vehiculeService.deleteVehicule(vehiculeId));

        assertEquals("Suppression impossible : le véhicule est déjà associé à des interventions existantes.", exception.getMessage());
        verify(vehiculeRepository, times(1)).existsById(vehiculeId);
        verify(interventionQuery, times(1)).listInterventionsByVehiculeId(vehiculeId);
        verify(vehiculeRepository, never()).deleteById(anyLong());
    }

    // ==================== Tests additionnels ====================

    @Test
    @DisplayName("Vérifier que le service est bien injecté")
    void testServiceInjection() {
        
        assertNotNull(vehiculeService);
        assertNotNull(vehiculeRepository);
        assertNotNull(vehiculeMapper);
        assertNotNull(interventionQuery);
    }
}

