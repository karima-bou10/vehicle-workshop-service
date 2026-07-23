package com.workshop.vehicle_service.mecanicien.service;

import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import com.workshop.vehicle_service.mecanicien.mapper.MecanicienMapper;
import com.workshop.vehicle_service.mecanicien.repository.MecanicienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MecanicienServiceImpTest {

    @Mock
    private MecanicienRepository mecanicienRepository;

    @Mock
    private MecanicienMapper mecanicienMapper;

    @InjectMocks
    private MecanicienServiceImp mecanicienService;

    private Mecanicien mecanicien;
    private MecanicienResponse mecanicienResponse;

    @BeforeEach
    void setUp() {
        mecanicien = new Mecanicien();
        mecanicien.setId(1L);
        mecanicien.setNom("Doe");
        mecanicien.setPrenom("John");
        mecanicien.setSpecialite("Moteur");
        mecanicien.setDisponible(true);

        mecanicienResponse = MecanicienResponse.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .specialite("Moteur")
                .disponible(true)
                .build();
    }

    @Test
    void testGetAllMecaniciens_ShouldReturnList() {
        when(mecanicienRepository.findAll()).thenReturn(List.of(mecanicien));
        when(mecanicienMapper.toResponseList(anyList())).thenReturn(List.of(mecanicienResponse));

        List<MecanicienResponse> result = mecanicienService.getAllMecaniciens();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getNom());
        verify(mecanicienRepository, times(1)).findAll();
    }

    @Test
    void testGetAllMecaniciens_ShouldReturnEmptyList() {
        when(mecanicienRepository.findAll()).thenReturn(Collections.emptyList());

        List<MecanicienResponse> result = mecanicienService.getAllMecaniciens();

        assertTrue(result.isEmpty());
        verify(mecanicienRepository, times(1)).findAll();
        verify(mecanicienMapper, never()).toResponseList(any());
    }

    @Test
    void testGetMecanicienById_ShouldReturnMecanicien() {
        when(mecanicienRepository.findById(1L)).thenReturn(Optional.of(mecanicien));
        when(mecanicienMapper.toResponse(mecanicien)).thenReturn(mecanicienResponse);

        MecanicienResponse result = mecanicienService.getMecanicienById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(mecanicienRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveMecanicien_ShouldReturnSavedMecanicien() {
        CreateMecanicienRequest request = CreateMecanicienRequest.builder()
                .nom("Doe")
                .prenom("John")
                .specialite("Moteur")
                .build();

        when(mecanicienMapper.toEntity(request)).thenReturn(mecanicien);
        when(mecanicienRepository.save(mecanicien)).thenReturn(mecanicien);
        when(mecanicienMapper.toResponse(mecanicien)).thenReturn(mecanicienResponse);

        MecanicienResponse result = mecanicienService.saveMecanicien(request);

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        verify(mecanicienRepository, times(1)).save(mecanicien);
    }

    @Test
    void testUpdateMecanicien_ShouldReturnUpdatedMecanicien() {
        UpdateMecanicienRequest request = UpdateMecanicienRequest.builder()
                .nom("Doe Updated")
                .prenom("John")
                .specialite("Moteur")
                .disponible(false)
                .build();

        when(mecanicienRepository.findById(1L)).thenReturn(Optional.of(mecanicien));
        // Note: La méthode du mapper renvoie void, pas besoin de when()
        when(mecanicienRepository.save(mecanicien)).thenReturn(mecanicien);
        when(mecanicienMapper.toResponse(mecanicien)).thenReturn(mecanicienResponse);

        MecanicienResponse result = mecanicienService.updateMecanicien(1L, request);

        assertNotNull(result);
        verify(mecanicienMapper, times(1)).updateEntityFromRequest(request, mecanicien);
        verify(mecanicienRepository, times(1)).save(mecanicien);
    }

    @Test
    void testUpdateMecanicien_ShouldThrowExceptionWhenNotFound() {
        UpdateMecanicienRequest request = new UpdateMecanicienRequest();
        when(mecanicienRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                mecanicienService.updateMecanicien(1L, request)
        );

        assertEquals("Mécanicien avec l'ID 1 non trouvé", exception.getMessage());
        verify(mecanicienRepository, never()).save(any());
    }

    @Test
    void testDeleteMecanicienById_ShouldCallRepositoryDelete() {
        doNothing().when(mecanicienRepository).deleteById(1L);

        mecanicienService.deleteMecanicienById(1L);

        verify(mecanicienRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetMecaniciensDisponibles_ShouldReturnList() {
        when(mecanicienRepository.findByDisponibleTrue()).thenReturn(List.of(mecanicien));
        when(mecanicienMapper.toResponseList(anyList())).thenReturn(List.of(mecanicienResponse));

        List<MecanicienResponse> result = mecanicienService.getMecaniciensDisponibles();

        assertFalse(result.isEmpty());
        verify(mecanicienRepository, times(1)).findByDisponibleTrue();
    }
}

