package com.workshop.vehicle_service.intervention.service;

import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.AffectationMecanicienRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.dtos.InterventionUpdateRequest;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.mapper.InterventionMapper;
import com.workshop.vehicle_service.intervention.service.Imp.BusinessException;
import com.workshop.vehicle_service.intervention.service.Imp.interventionServiceImpl;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.mecanicien.repository.MecanicienRepository;
import com.workshop.vehicle_service.vehicule.repository.VehiculeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterventionServiceImplTest {

    @Mock
    private InterventionRepository interventionRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private MecanicienRepository mecanicienRepository;

    @Mock
    private InterventionMapper interventionMapper;

    @Mock
    private StatutInterventionService statutInterventionService;

    @InjectMocks
    private interventionServiceImpl interventionService;

    private Intervention intervention;
    private Mecanicien ancienMecanicien;
    private Mecanicien nouveauMecanicien;

    @BeforeEach
    void setUp() {
        intervention = new Intervention();
        intervention.setId(1L);
        intervention.setStatut(StatutIntervention.DEVIS_A_VALIDER);

        ancienMecanicien = new Mecanicien();
        ancienMecanicien.setId(10L);
        ancienMecanicien.setDisponible(false);

        nouveauMecanicien = new Mecanicien();
        nouveauMecanicien.setId(20L);
        nouveauMecanicien.setDisponible(true);
    }

    @Test
    void affectationMecanicien_shouldAffectAvailableMechanic()
            throws BusinessException {

        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        Intervention saved = new Intervention();
        saved.setId(1L);
        saved.setStatut(StatutIntervention.DEVIS_A_VALIDER);
        saved.setMecanicien(nouveauMecanicien);

        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));

        when(mecanicienRepository.findById(20L))
                .thenReturn(Optional.of(nouveauMecanicien));

        when(interventionRepository.save(intervention))
                .thenReturn(saved);

        when(interventionMapper.toResponse(saved))
                .thenReturn(expected);

        InterventionResponse result =
                interventionService
                        .affectationMecanicienIntervention(1L, request);

        assertSame(nouveauMecanicien, intervention.getMecanicien());
        assertSame(expected, result);

        verify(interventionRepository).save(intervention);
        verify(interventionMapper).toResponse(saved);
    }

    @Test
    void affectationMecanicien_shouldRejectUnavailableMechanic() {
        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        nouveauMecanicien.setDisponible(false);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));
        when(mecanicienRepository.findById(20L))
                .thenReturn(Optional.of(nouveauMecanicien));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> interventionService.affectationMecanicienIntervention(1L, request)
        );

        assertEquals("Le mécanicien n'est pas disponible", exception.getMessage());
        verify(interventionRepository, never()).save(any());
        verify(mecanicienRepository, never()).save(any());
    }

    @Test
    void affectationMecanicien_shouldAllowModificationBeforeRepair()
            throws BusinessException {

        intervention.setMecanicien(ancienMecanicien);

        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        Intervention saved = new Intervention();
        saved.setId(1L);
        saved.setStatut(StatutIntervention.DEVIS_A_VALIDER);
        saved.setMecanicien(nouveauMecanicien);

        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));

        when(mecanicienRepository.findById(20L))
                .thenReturn(Optional.of(nouveauMecanicien));

        when(interventionRepository.save(intervention))
                .thenReturn(saved);

        when(interventionMapper.toResponse(saved))
                .thenReturn(expected);

        InterventionResponse result =
                interventionService
                        .affectationMecanicienIntervention(1L, request);

        assertSame(nouveauMecanicien, intervention.getMecanicien());
        assertSame(expected, result);

        verify(interventionRepository).save(intervention);
        verify(interventionMapper).toResponse(saved);
    }

    @Test
    void affectationMecanicien_shouldRejectModificationWhenInRepair() {
        intervention.setStatut(StatutIntervention.EN_REPARATION);
        intervention.setMecanicien(ancienMecanicien);

        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> interventionService.affectationMecanicienIntervention(1L, request)
        );

        assertEquals(
                "Impossible de modifier l'affectation à ce stade de l'intervention",
                exception.getMessage()
        );

        verify(mecanicienRepository, never()).findById(anyLong());
        verify(interventionRepository, never()).save(any());
    }

    @Test
    void affectationMecanicien_shouldRejectModificationWhenTerminee() {
        intervention.setStatut(StatutIntervention.TERMINEE);

        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));

        assertThrows(
                BusinessException.class,
                () -> interventionService.affectationMecanicienIntervention(1L, request)
        );

        verify(interventionRepository, never()).save(any());
    }

    @Test
    void affectationMecanicien_shouldRejectModificationWhenRestituee() {
        intervention.setStatut(StatutIntervention.RESTITUEE);

        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));

        assertThrows(
                BusinessException.class,
                () -> interventionService.affectationMecanicienIntervention(1L, request)
        );

        verify(interventionRepository, never()).save(any());
    }

    @Test
    void affectationMecanicien_shouldRejectModificationWhenAnnulee() {
        intervention.setStatut(StatutIntervention.ANNULEE);

        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));

        assertThrows(
                BusinessException.class,
                () -> interventionService.affectationMecanicienIntervention(1L, request)
        );

        verify(interventionRepository, never()).save(any());
    }

    @Test
    void affectationMecanicien_shouldRejectUnknownIntervention() {
        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> interventionService.affectationMecanicienIntervention(1L, request)
        );

        assertEquals("Intervention introuvable", exception.getMessage());
        verify(mecanicienRepository, never()).findById(anyLong());
    }

    @Test
    void affectationMecanicien_shouldRejectUnknownMechanic() {
        AffectationMecanicienRequest request =
                new AffectationMecanicienRequest(20L);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));
        when(mecanicienRepository.findById(20L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> interventionService.affectationMecanicienIntervention(1L, request)
        );

        assertEquals("Mécanicien introuvable", exception.getMessage());
        verify(interventionRepository, never()).save(any());
    }

    @Test
    void modifierUneIntervention_shouldRejectWhenStatusIsDevis() {
        intervention.setStatut(StatutIntervention.DEVIS_A_VALIDER);

        InterventionUpdateRequest request =
                mock(InterventionUpdateRequest.class);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));

        assertThrows(
                BusinessException.class,
                () -> interventionService.modifierUneIntervention(request, 1L)
        );

        verify(interventionMapper, never()).updateEntity(any(), any());
        verify(interventionRepository, never()).save(any());
    }

    @Test
    void modifierUneIntervention_shouldAllowWhenStatusIsRecue() throws BusinessException {
        intervention.setStatut(StatutIntervention.RECUE);

        InterventionUpdateRequest request =
                mock(InterventionUpdateRequest.class);

        InterventionResponse expected = mock(InterventionResponse.class);

        when(interventionRepository.findById(1L))
                .thenReturn(Optional.of(intervention));
        when(interventionRepository.save(intervention))
                .thenReturn(intervention);
        when(interventionMapper.toResponse(intervention))
                .thenReturn(expected);

        InterventionResponse result =
                interventionService.modifierUneIntervention(request, 1L);

        verify(interventionMapper).updateEntity(request, intervention);
        verify(interventionRepository).save(intervention);
        assertSame(expected, result);
    }


}
