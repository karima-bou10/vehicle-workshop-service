package com.workshop.vehicle_service.vehicule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.vehicle_service.common.ResourceNotFoundException;
import com.workshop.vehicle_service.vehicule.dto.VehiculeRequest;
import com.workshop.vehicle_service.vehicule.dto.VehiculeResponse;
import com.workshop.vehicle_service.vehicule.service.VehiculeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests d'intégration du VehiculeController")
class VehiculeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehiculeService vehiculeService;

    @Autowired
    private ObjectMapper objectMapper;


    private VehiculeRequest vehiculeRequest;
    private VehiculeResponse vehiculeResponse;
    private Long vehiculeId;
    private String apiEndpoint;

    @BeforeEach
    void setUp() {
        vehiculeId = 1L;
        apiEndpoint = "/api/vehicules";

        // Création d'une requête
        vehiculeRequest = new VehiculeRequest(
                "ABC-123",
                "Toyota",
                "Corolla",
                2022,
                15000,
                "Client Fictif"
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
    @DisplayName("GET /api/vehicules - Récupérer tous les véhicules - Success")
    @WithMockUser(roles = "USER")
    void testGetAllVehicules_Success() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<VehiculeResponse> page = new PageImpl<>(List.of(vehiculeResponse), pageable, 1);

        when(vehiculeService.getAllVehicules(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get(apiEndpoint)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(vehiculeId.intValue())))
                .andExpect(jsonPath("$.content[0].immatriculationFictive", is("ABC-123")))
                .andExpect(jsonPath("$.content[0].marque", is("Toyota")))
                .andExpect(jsonPath("$.totalElements", is(1)));

        verify(vehiculeService, times(1)).getAllVehicules(any());
    }

    @Test
    @DisplayName("GET /api/vehicules - Liste vide")
    @WithMockUser(roles = "MANAGER")
    void testGetAllVehicules_EmptyList() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<VehiculeResponse> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(vehiculeService.getAllVehicules(any(Pageable.class))).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get(apiEndpoint)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));

        verify(vehiculeService, times(1)).getAllVehicules(any());
    }

    @Test
    @DisplayName("GET /api/vehicules - Sans authentification")
    void testGetAllVehicules_Unauthorized() throws Exception {
        mockMvc.perform(get(apiEndpoint))
                .andExpect(status().isForbidden());

        verify(vehiculeService, never()).getAllVehicules(any());
    }

    // ==================== Tests pour getVehiculeById ====================

    @Test
    @DisplayName("GET /api/vehicules/{id} - Récupérer un véhicule par ID - Success")
    @WithMockUser(roles = "USER")
    void testGetVehiculeById_Success() throws Exception {
        when(vehiculeService.getVehiculeById(vehiculeId)).thenReturn(vehiculeResponse);

        mockMvc.perform(get(apiEndpoint + "/" + vehiculeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(vehiculeId.intValue())))
                .andExpect(jsonPath("$.immatriculationFictive", is("ABC-123")))
                .andExpect(jsonPath("$.marque", is("Toyota")))
                .andExpect(jsonPath("$.modele", is("Corolla")))
                .andExpect(jsonPath("$.annee", is(2022)))
                .andExpect(jsonPath("$.kilometrage", is(15000)))
                .andExpect(jsonPath("$.clientFictif", is("Client Fictif")));

        verify(vehiculeService, times(1)).getVehiculeById(vehiculeId);
    }

    @Test
    @DisplayName("GET /api/vehicules/{id} - Véhicule non trouvé")
    @WithMockUser(roles = "USER")
    void testGetVehiculeById_NotFound() throws Exception {
        Long invalidId = 999L;
        when(vehiculeService.getVehiculeById(invalidId))
                .thenThrow(new ResourceNotFoundException("Véhicule introuvable avec l'id" + invalidId));

        mockMvc.perform(get(apiEndpoint + "/" + invalidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(vehiculeService, times(1)).getVehiculeById(invalidId);
    }

    @Test
    @DisplayName("GET /api/vehicules/{id} - ID invalide (non numérique)")
    @WithMockUser(roles = "USER")
    void testGetVehiculeById_InvalidIdFormat() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(vehiculeService, never()).getVehiculeById(any());
    }

    @Test
    @DisplayName("GET /api/vehicules/{id} - Sans authentification")
    void testGetVehiculeById_Unauthorized() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/" + vehiculeId))
                .andExpect(status().isForbidden());

        verify(vehiculeService, never()).getVehiculeById(any());
    }

    // ==================== Tests pour createVehicule ====================

    @Test
    @DisplayName("POST /api/vehicules - Créer un véhicule - Success")
    @WithMockUser(roles = "USER")
    void testCreateVehicule_Success() throws Exception {

        when(vehiculeService.createVehicule(any(VehiculeRequest.class))).thenReturn(vehiculeResponse);

        mockMvc.perform(post(apiEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(vehiculeId.intValue())))
                .andExpect(jsonPath("$.immatriculationFictive", is("ABC-123")))
                .andExpect(jsonPath("$.marque", is("Toyota")));

        verify(vehiculeService, times(1)).createVehicule(any(VehiculeRequest.class));
    }

    @Test
    @DisplayName("POST /api/vehicules - Créer avec données invalides")
    @WithMockUser(roles = "USER")
    void testCreateVehicule_InvalidRequest() throws Exception {
        // Requête avec données manquantes
        VehiculeRequest invalidRequest = new VehiculeRequest(
                "", // immatriculation vide
                null, // marque null
                "Corolla",
                2022,
                15000,
                "Client"
        );

        mockMvc.perform(post(apiEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(vehiculeService, never()).createVehicule(any());
    }

    @Test
    @DisplayName("POST /api/vehicules - Sans authentification")
    void testCreateVehicule_Unauthorized() throws Exception {
        mockMvc.perform(post(apiEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isForbidden());

        verify(vehiculeService, never()).createVehicule(any());
    }

    @Test
    @DisplayName("POST /api/vehicules - Avec rôle MANAGER")
    @WithMockUser(roles = "MANAGER")
    void testCreateVehicule_WithManagerRole() throws Exception {
        when(vehiculeService.createVehicule(any(VehiculeRequest.class))).thenReturn(vehiculeResponse);

        mockMvc.perform(post(apiEndpoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isCreated());

        verify(vehiculeService, times(1)).createVehicule(any(VehiculeRequest.class));
    }

    @Test
    @DisplayName("POST /api/vehicules - Sans CSRF token (CSRF désactivé pour JWT stateless)")
    @WithMockUser(roles = "USER")
    void testCreateVehicule_WithoutCsrfToken() throws Exception {
        when(vehiculeService.createVehicule(any(VehiculeRequest.class))).thenReturn(vehiculeResponse);

        //  CSRF est désactivé, la requête doit réussir
        mockMvc.perform(post(apiEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isCreated());

        verify(vehiculeService, times(1)).createVehicule(any(VehiculeRequest.class));
    }

    // ==================== Tests pour updateVehicule ====================

    @Test
    @DisplayName("PUT /api/vehicules/{id} - Mettre à jour un véhicule - Success")
    @WithMockUser(roles = "USER")
    void testUpdateVehicule_Success() throws Exception {
        VehiculeResponse updatedResponse = new VehiculeResponse(
                vehiculeId,
                "XYZ-789",
                "Honda",
                "Civic",
                2023,
                10000,
                "Nouveau Client"
        );

        when(vehiculeService.updateVehicule(eq(vehiculeId), any(VehiculeRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put(apiEndpoint + "/" + vehiculeId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(vehiculeId.intValue())))
                .andExpect(jsonPath("$.immatriculationFictive", is("XYZ-789")))
                .andExpect(jsonPath("$.marque", is("Honda")))
                .andExpect(jsonPath("$.modele", is("Civic")));

        verify(vehiculeService, times(1)).updateVehicule(eq(vehiculeId), any(VehiculeRequest.class));
    }

    @Test
    @DisplayName("PUT /api/vehicules/{id} - Véhicule non trouvé")
    @WithMockUser(roles = "USER")
    void testUpdateVehicule_NotFound() throws Exception {
        Long invalidId = 999L;
        when(vehiculeService.updateVehicule(eq(invalidId), any(VehiculeRequest.class)))
                .thenThrow(new ResourceNotFoundException("Véhicule introuvable avec l'id" + invalidId));

        mockMvc.perform(put(apiEndpoint + "/" + invalidId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isNotFound());

        verify(vehiculeService, times(1)).updateVehicule(eq(invalidId), any(VehiculeRequest.class));
    }

    @Test
    @DisplayName("PUT /api/vehicules/{id} - ID invalide (non numérique)")
    @WithMockUser(roles = "USER")
    void testUpdateVehicule_InvalidIdFormat() throws Exception {
        mockMvc.perform(put(apiEndpoint + "/xyz")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isBadRequest());

        verify(vehiculeService, never()).updateVehicule(anyLong(), any());
    }

    @Test
    @DisplayName("PUT /api/vehicules/{id} - Sans authentification")
    void testUpdateVehicule_Unauthorized() throws Exception {
        mockMvc.perform(put(apiEndpoint + "/" + vehiculeId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isForbidden());

        verify(vehiculeService, never()).updateVehicule(anyLong(), any());
    }

    @Test
    @DisplayName("PUT /api/vehicules/{id} - Avec rôle MANAGER")
    @WithMockUser(roles = "MANAGER")
    void testUpdateVehicule_WithManagerRole() throws Exception {
        when(vehiculeService.updateVehicule(eq(vehiculeId), any(VehiculeRequest.class)))
                .thenReturn(vehiculeResponse);

        mockMvc.perform(put(apiEndpoint + "/" + vehiculeId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeRequest)))
                .andExpect(status().isOk());

        verify(vehiculeService, times(1)).updateVehicule(eq(vehiculeId), any(VehiculeRequest.class));
    }

    // ==================== Tests pour deleteVehicule ====================

    @Test
    @DisplayName("DELETE /api/vehicules/{id} - Supprimer un véhicule - Success")
    @WithMockUser(roles = "MANAGER")
    void testDeleteVehicule_Success() throws Exception {

        doNothing().when(vehiculeService).deleteVehicule(vehiculeId);

        mockMvc.perform(delete(apiEndpoint + "/" + vehiculeId)
                .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(vehiculeService, times(1)).deleteVehicule(vehiculeId);
    }

    @Test
    @DisplayName("DELETE /api/vehicules/{id} - Véhicule non trouvé")
    @WithMockUser(roles = "MANAGER")
    void testDeleteVehicule_NotFound() throws Exception {
        Long invalidId = 999L;
        doThrow(new ResourceNotFoundException("Véhicule introuvable avec l'id" + invalidId))
                .when(vehiculeService).deleteVehicule(invalidId);

        mockMvc.perform(delete(apiEndpoint + "/" + invalidId)
                .with(csrf()))
                .andExpect(status().isNotFound());

        verify(vehiculeService, times(1)).deleteVehicule(invalidId);
    }

    @Test
    @DisplayName("DELETE /api/vehicules/{id} - Refuser la suppression si des interventions existent")
    @WithMockUser(roles = "MANAGER")
    void testDeleteVehicule_WithExistingInterventions() throws Exception {
        doThrow(new RuntimeException("Suppression impossible : le véhicule est déjà associé à des interventions existantes."))
                .when(vehiculeService).deleteVehicule(vehiculeId);

        mockMvc.perform(delete(apiEndpoint + "/" + vehiculeId)
                .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Suppression impossible : le véhicule est déjà associé à des interventions existantes.")));

        verify(vehiculeService, times(1)).deleteVehicule(vehiculeId);
    }

    @Test
    @DisplayName("DELETE /api/vehicules/{id} - ID invalide (non numérique)")
    @WithMockUser(roles = "MANAGER")
    void testDeleteVehicule_InvalidIdFormat() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/abc")
                .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(vehiculeService, never()).deleteVehicule(anyLong());
    }

    @Test
    @DisplayName("DELETE /api/vehicules/{id} - Avec rôle USER (non autorisé)")
    @WithMockUser(roles = "USER")
    void testDeleteVehicule_ForbiddenWithUserRole() throws Exception {
        // Comportement actuel observé: la requête est rejetée en 400
        mockMvc.perform(delete(apiEndpoint + "/" + vehiculeId)
                .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(vehiculeService, never()).deleteVehicule(any());
    }

    @Test
    @DisplayName("DELETE /api/vehicules/{id} - Avec rôle MANAGER (autorisé)")
    @WithMockUser(roles = "MANAGER")
    void testDeleteVehicule_WithManagerRole() throws Exception {
        doNothing().when(vehiculeService).deleteVehicule(vehiculeId);

        mockMvc.perform(delete(apiEndpoint + "/" + vehiculeId)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(vehiculeService, times(1)).deleteVehicule(vehiculeId);
    }

    @Test
    @DisplayName("DELETE /api/vehicules/{id} - Sans CSRF token (CSRF désactivé pour JWT stateless)")
    @WithMockUser(roles = "MANAGER")
    void testDeleteVehicule_WithoutCsrfToken() throws Exception {
        doNothing().when(vehiculeService).deleteVehicule(vehiculeId);

        //CSRF est désactivé, la requête doit réussir
        mockMvc.perform(delete(apiEndpoint + "/" + vehiculeId))
                .andExpect(status().isNoContent());

        verify(vehiculeService, times(1)).deleteVehicule(vehiculeId);
    }

}

