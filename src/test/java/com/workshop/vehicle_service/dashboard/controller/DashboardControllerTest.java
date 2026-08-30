package com.workshop.vehicle_service.dashboard.controller;

import com.workshop.vehicle_service.auth.config.JwtAuthenticationFilter;
import com.workshop.vehicle_service.dashboard.dto.ChargeMecanicienDto;
import com.workshop.vehicle_service.dashboard.dto.DashboardResumeDto;
import com.workshop.vehicle_service.dashboard.dto.StatutCompteurDto;
import com.workshop.vehicle_service.dashboard.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private DashboardResumeDto dashboardResumeDto;
    private ChargeMecanicienDto chargeMecanicienDto;

    @BeforeEach
    void setUp() {
        StatutCompteurDto statutDto = new StatutCompteurDto("EN_REPARATION", 5L);
        dashboardResumeDto = new DashboardResumeDto(2L, 1L, 5l, 10L, 0L, List.of(statutDto));
        chargeMecanicienDto = new ChargeMecanicienDto(1L, "Benali", 3L);
    }

    // =========================================================================
    // TESTS POUR GET /api/dashboard/resume
    // =========================================================================

    @Test
    void testGetResume_ShouldReturn200() throws Exception {
        when(dashboardService.calculerResumeGlobal()).thenReturn(dashboardResumeDto);

        mockMvc.perform(get("/api/dashboard/resume")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recuesAujourdhui").value(2))
                .andExpect(jsonPath("$.enDiagnostic").value(1))
                .andExpect(jsonPath("$.enReparation").value(5))
                .andExpect(jsonPath("$.terminees").value(10))
                .andExpect(jsonPath("$.retardsRestitution").value(0))
                .andExpect(jsonPath("$.repartitionStatuts.size()").value(1))
                .andExpect(jsonPath("$.repartitionStatuts[0].statut").value("EN_REPARATION"))
                .andExpect(jsonPath("$.repartitionStatuts[0].nombre").value(5));
    }

    @Test
    void testGetResume_WhenNoInterventions_ShouldReturn200() throws Exception {
        // Cas où l'atelier est complètement vide
        DashboardResumeDto emptyResume = new DashboardResumeDto(
                0L, 0L, 0L, 0L, 0L, Collections.emptyList()
        );

        when(dashboardService.calculerResumeGlobal()).thenReturn(emptyResume);

        mockMvc.perform(get("/api/dashboard/resume")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recuesAujourdhui").value(0))
                .andExpect(jsonPath("$.enDiagnostic").value(0))
                .andExpect(jsonPath("$.enReparation").value(0))
                .andExpect(jsonPath("$.terminees").value(0))
                .andExpect(jsonPath("$.retardsRestitution").value(0))
                .andExpect(jsonPath("$.repartitionStatuts").isEmpty());
    }

    @Test
    void testGetResume_WhenServiceThrowsException_ShouldReturn400() throws Exception {
        // Cas où une erreur inattendue survient dans le service
        when(dashboardService.calculerResumeGlobal()).thenThrow(new RuntimeException("Erreur inattendue"));

        mockMvc.perform(get("/api/dashboard/resume")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // TESTS POUR GET /api/dashboard/charge-mecaniciens
    // =========================================================================

    @Test
    void testGetChargeMecaniciens_ShouldReturn200() throws Exception {
        when(dashboardService.calculerChargeMecaniciens()).thenReturn(List.of(chargeMecanicienDto));

        mockMvc.perform(get("/api/dashboard/charge-mecaniciens")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].mecanicienId").value(1L))
                .andExpect(jsonPath("$[0].mecanicienNom").value("Benali"))
                .andExpect(jsonPath("$[0].nombreInterventionsActives").value(3L));
    }

    @Test
    void testGetChargeMecaniciens_WhenNoMecaniciens_ShouldReturn200() throws Exception {
        // Cas où il n'y a aucun mécanicien enregistré dans la base
        when(dashboardService.calculerChargeMecaniciens()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/dashboard/charge-mecaniciens")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetChargeMecaniciens_WhenServiceThrowsException_ShouldReturn400() throws Exception {
        // Cas où la récupération de la charge échoue
        when(dashboardService.calculerChargeMecaniciens()).thenThrow(new RuntimeException("Erreur lors du calcul de la charge"));

        mockMvc.perform(get("/api/dashboard/charge-mecaniciens")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}