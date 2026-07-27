package com.workshop.vehicle_service.mecanicien.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.vehicle_service.auth.config.JwtAuthenticationFilter;
import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.service.MecanicienService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MecanicienController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive Spring Security pour les tests unitaires web
class MecanicienControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private MecanicienService mecanicienService;

    private MecanicienResponse mecanicienResponse;

    @BeforeEach
    void setUp() {
        mecanicienResponse = MecanicienResponse.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .specialite("Moteur")
                .disponible(true)
                .build();
    }

    @Test
    void testGetAllMecaniciens_ShouldReturn200() throws Exception {
        Page<MecanicienResponse> pageResponse = new PageImpl<>(List.of(mecanicienResponse),PageRequest.of(0, 10),1);

        when(mecanicienService.getAllMecaniciens(any(Pageable.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/mecaniciens/getAll")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].nom").value("Doe"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void testGetMecanicienById_ShouldReturn200() throws Exception {
        when(mecanicienService.getMecanicienById(1L)).thenReturn(mecanicienResponse);

        mockMvc.perform(get("/api/mecaniciens/get/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.prenom").value("John"));
    }

    @Test
    void testCreateMecanicien_ShouldReturn201() throws Exception {
        CreateMecanicienRequest request = CreateMecanicienRequest.builder()
                .nom("Doe")
                .prenom("John")
                .specialite("Moteur")
                .build();

        when(mecanicienService.saveMecanicien(any(CreateMecanicienRequest.class))).thenReturn(mecanicienResponse);

        mockMvc.perform(post("/api/mecaniciens/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.specialite").value("Moteur"));
    }

    @Test
    void testUpdateMecanicien_ShouldReturn200() throws Exception {
        UpdateMecanicienRequest request = UpdateMecanicienRequest.builder()
                .nom("Doe")
                .prenom("John")
                .specialite("Electrique")
                .disponible(false)
                .build();

        MecanicienResponse updatedResponse = MecanicienResponse.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .specialite("Electrique")
                .disponible(false)
                .build();

        when(mecanicienService.updateMecanicien(eq(1L), any(UpdateMecanicienRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/mecaniciens/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialite").value("Electrique"))
                .andExpect(jsonPath("$.disponible").value(false));
    }

    @Test
    void testDeleteMecanicien_ShouldReturn200() throws Exception {
        doNothing().when(mecanicienService).deleteMecanicienById(1L);

        mockMvc.perform(delete("/api/mecaniciens/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Mecanicien deleted successfully"));
    }

    @Test
    void testGetMecaniciensDisponibles_ShouldReturn200() throws Exception {
        when(mecanicienService.getMecaniciensDisponibles()).thenReturn(List.of(mecanicienResponse));

        mockMvc.perform(get("/api/mecaniciens/getAllDisponibles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].disponible").value(true));
    }
}
