package com.workshop.vehicle_service.intervention.controller;

import com.workshop.vehicle_service.intervention.dtos.*;
import com.workshop.vehicle_service.intervention.service.Imp.BusinessException;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.intervention.service.StatutInterventionService;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterventionControllerTest {

    @Mock
    private InterventionService interventionService;

    @Mock
    private StatutInterventionService statutInterventionService;

    @InjectMocks
    private InterventionController interventionController;

    @Test
    void recupererListInterventions_shouldReturnList() {
        List<InterventionResponse> expected = List.of();
        when(interventionService.recupererListInterventions()).thenReturn(expected);

        List<InterventionResponse> result =
                interventionController.recupererListInterventions();

        assertSame(expected, result);
        verify(interventionService).recupererListInterventions();
    }

    @Test
    void recupererUneIntervention_shouldReturnIntervention() {
        Long id = 1L;
        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionService.recupererUneIntervention(id)).thenReturn(expected);

        InterventionResponse result =
                interventionController.recupererUneIntervention(id);

        assertSame(expected, result);
        verify(interventionService).recupererUneIntervention(id);
    }

    @Test
    void affecterMecanicien_shouldReturnUpdatedIntervention() throws BusinessException {
        Long id = 1L;
        AffectationMecanicienRequest request =
                mock(AffectationMecanicienRequest.class);
        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionService.affectationMecanicienIntervention(id, request))
                .thenReturn(expected);

        InterventionResponse result =
                interventionController.affecterMecanicien(id, request);

        assertSame(expected, result);
        verify(interventionService)
                .affectationMecanicienIntervention(id, request);
    }

    @Test
    void affecterMecanicien_shouldPropagateBusinessException() throws BusinessException {
        Long id = 1L;
        AffectationMecanicienRequest request =
                mock(AffectationMecanicienRequest.class);

        when(interventionService.affectationMecanicienIntervention(id, request))
                .thenThrow(new BusinessException(
                        "Impossible de modifier l'affectation à ce stade de l'intervention"));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> interventionController.affecterMecanicien(id, request)
        );

        assertEquals(
                "Impossible de modifier l'affectation à ce stade de l'intervention",
                exception.getMessage()
        );
    }

    @Test
    void ajouterDiagnostic_shouldReturnUpdatedIntervention() throws BusinessException {
        Long id = 1L;
        DiagnosticRequest request = mock(DiagnosticRequest.class);
        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionService.ajouterDiagnostic(id, request))
                .thenReturn(expected);

        InterventionResponse result =
                interventionController.ajouterDiagnostic(id, request);

        assertSame(expected, result);
        verify(interventionService).ajouterDiagnostic(id, request);
    }

    @Test
    void ajouterDevis_shouldReturnUpdatedIntervention() {
        Long id = 1L;
        DevisRequest request = mock(DevisRequest.class);
        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionService.ajouterDevis(id, request))
                .thenReturn(expected);

        InterventionResponse result =
                interventionController.ajouterDevis(id, request);

        assertSame(expected, result);
        verify(interventionService).ajouterDevis(id, request);
    }

    @Test
    void changerStatut_shouldReturn200AndUpdatedIntervention() {
        Long id = 1L;
        ChangementStatutRequest request = mock(ChangementStatutRequest.class);
        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionService.changerStatut(id, request))
                .thenReturn(expected);

        var response = interventionController.changerStatut(id, request);

        assertEquals(200, response.getStatusCode().value());
        assertSame(expected, response.getBody());
        verify(interventionService).changerStatut(id, request);
    }

    @Test
    void supprimerUneIntervention_shouldReturnDeletedIntervention() {
        Long id = 1L;
        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionService.supprimerUneIntervention(id))
                .thenReturn(expected);

        InterventionResponse result =
                interventionController.supprimerUneIntervention(id);

        assertSame(expected, result);
        verify(interventionService).supprimerUneIntervention(id);
    }

    @Test
    void listInterventionsByVehiculeId_shouldReturnList() {
        Long vehiculeId = 5L;
        List<InterventionResponse> expected = List.of();

        when(interventionService.listInterventionsByVehiculeId(vehiculeId))
                .thenReturn(expected);

        List<InterventionResponse> result =
                interventionController.listInterventionsByVehiculeId(vehiculeId);

        assertSame(expected, result);
        verify(interventionService).listInterventionsByVehiculeId(vehiculeId);
    }

    @Test
    void listInterventionsByMecanicienId_shouldReturnList() {
        Long mecanicienId = 7L;
        List<InterventionResponse> expected = List.of();

        when(interventionService.listInterventionsByMecanicienId(mecanicienId))
                .thenReturn(expected);

        List<InterventionResponse> result =
                interventionController.listInterventionsByMecanicienId(mecanicienId);

        assertSame(expected, result);
        verify(interventionService).listInterventionsByMecanicienId(mecanicienId);
    }

    @Test
    void recupererHistoriqueInterventions_shouldReturnList() {
        List<InterventionResponse> expected = List.of();

        when(interventionService.recupererHistoriqueComplet())
                .thenReturn(expected);

        List<InterventionResponse> result =
                interventionController.recupererHistoriqueInterventions();

        assertSame(expected, result);
        verify(interventionService).recupererHistoriqueComplet();
    }

    @Test
    void getInterventionsEnRetard_shouldReturnList() {
        List<InterventionResponse> expected = List.of();

        when(interventionService.getInterventionsRestitueEnRetard())
                .thenReturn(expected);

        List<InterventionResponse> result =
                interventionController.getInterventionsEnRetard();

        assertSame(expected, result);
        verify(interventionService).getInterventionsRestitueEnRetard();
    }
}
