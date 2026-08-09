package com.workshop.vehicle_service.dashboard.service;

import com.workshop.vehicle_service.dashboard.dto.ChargeMecanicienDto;
import com.workshop.vehicle_service.dashboard.dto.DashboardResumeDto;
import com.workshop.vehicle_service.intervention.Repository.InterventionRepository;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.service.InterventionService;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.service.MecanicienService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImpTest {
    @Mock
    private InterventionService interventionService;

    @Mock
    private MecanicienService mecanicienService;

    @Mock
    private InterventionRepository interventionRepository;

    @InjectMocks
    private DashboardServiceImp dashboardService;

    @Nested
    class CalculerResumeGlobalTests {

        @Test
        void calculerResumeGlobal_CasNominal() {
            // Given
            InterventionResponse i1 = mock(InterventionResponse.class);
            when(i1.dateDepot()).thenReturn(LocalDateTime.now());
            when(i1.statut()).thenReturn(StatutIntervention.DIAGNOSTIC_EN_COURS);

            InterventionResponse i2 = mock(InterventionResponse.class);
            when(i2.dateDepot()).thenReturn(LocalDateTime.now());
            when(i2.statut()).thenReturn(StatutIntervention.EN_REPARATION);

            InterventionResponse i3 = mock(InterventionResponse.class);
            when(i3.dateDepot()).thenReturn(LocalDateTime.now().minusDays(2));
            when(i3.statut()).thenReturn(StatutIntervention.TERMINEE);

            when(interventionService.recupererListInterventions()).thenReturn(List.of(i1, i2, i3));
            when(interventionRepository.countByDateRestitutionPrevueBeforeAndStatutIsNot(
                    any(LocalDateTime.class), eq(StatutIntervention.RESTITUEE)))
                    .thenReturn(1L);

            // When
            DashboardResumeDto result = dashboardService.calculerResumeGlobal();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.recuesAujourdhui()).isEqualTo(2);
            assertThat(result.enDiagnostic()).isEqualTo(1);
            assertThat(result.enReparation()).isEqualTo(1);
            assertThat(result.terminees()).isEqualTo(1);
            assertThat(result.retardsRestitution()).isEqualTo(1);
            assertThat(result.repartitionStatuts()).hasSize(3);

            verify(interventionService, times(1)).recupererListInterventions();
            verify(interventionRepository, times(1))
                    .countByDateRestitutionPrevueBeforeAndStatutIsNot(any(LocalDateTime.class), eq(StatutIntervention.RESTITUEE));
        }

        @Test
        void calculerResumeGlobal_ListeVide() {
            // Given
            when(interventionService.recupererListInterventions()).thenReturn(Collections.emptyList());
            when(interventionRepository.countByDateRestitutionPrevueBeforeAndStatutIsNot(
                    any(LocalDateTime.class), eq(StatutIntervention.RESTITUEE)))
                    .thenReturn(0L);

            // When
            DashboardResumeDto result = dashboardService.calculerResumeGlobal();

            // Then
            assertThat(result.recuesAujourdhui()).isZero();
            assertThat(result.enDiagnostic()).isZero();
            assertThat(result.enReparation()).isZero();
            assertThat(result.terminees()).isZero();
            assertThat(result.retardsRestitution()).isZero();
            assertThat(result.repartitionStatuts()).isEmpty();
        }

        @Test
        void calculerResumeGlobal_DateDepotNull() {
            // Given
            InterventionResponse i1 = mock(InterventionResponse.class);
            when(i1.dateDepot()).thenReturn(null);
            when(i1.statut()).thenReturn(StatutIntervention.DIAGNOSTIC_EN_COURS);

            when(interventionService.recupererListInterventions()).thenReturn(List.of(i1));
            when(interventionRepository.countByDateRestitutionPrevueBeforeAndStatutIsNot(
                    any(LocalDateTime.class), eq(StatutIntervention.RESTITUEE)))
                    .thenReturn(0L);

            // When
            DashboardResumeDto result = dashboardService.calculerResumeGlobal();

            // Then
            assertThat(result.recuesAujourdhui()).isZero();
            assertThat(result.enDiagnostic()).isEqualTo(1);
            assertThat(result.repartitionStatuts()).hasSize(1);
        }
    }

    @Nested
    class CalculerChargeMecaniciensTests {

        @Test
        void calculerChargeMecaniciens_CasNominal() {
            // Given
            MecanicienResponse m1 = new MecanicienResponse();
            m1.setId(1L);
            m1.setNom("Benali");
            m1.setPrenom("Ahmed");

            MecanicienResponse m2 = new MecanicienResponse();
            m2.setId(2L);
            m2.setNom("Sabri");
            m2.setPrenom("Jawad");

            when(mecanicienService.getAllMecaniciens(PageRequest.of(0, 10)))
                    .thenReturn(new PageImpl<>(List.of(m1, m2)));

            // Interventions pour m1
            InterventionResponse i1 = mock(InterventionResponse.class);
            when(i1.mecanicienId()).thenReturn(1L);
            when(i1.statut()).thenReturn(StatutIntervention.EN_REPARATION);

            InterventionResponse i2 = mock(InterventionResponse.class);
            when(i2.mecanicienId()).thenReturn(1L);
            when(i2.statut()).thenReturn(StatutIntervention.TERMINEE);

            // Interventions pour m2
            InterventionResponse i3 = mock(InterventionResponse.class);
            when(i3.mecanicienId()).thenReturn(2L);
            when(i3.statut()).thenReturn(StatutIntervention.ANNULEE);

            // Intervention sans mécanicien assigné
            InterventionResponse i4 = mock(InterventionResponse.class);
            when(i4.mecanicienId()).thenReturn(null);

            when(interventionService.recupererListInterventions()).thenReturn(List.of(i1, i2, i3, i4));

            // When
            List<ChargeMecanicienDto> result = dashboardService.calculerChargeMecaniciens();

            // Then
            assertThat(result).hasSize(2);

            ChargeMecanicienDto chargeM1 = result.stream().filter(c -> c.mecanicienId().equals(1L)).findFirst().orElseThrow();
            assertThat(chargeM1.mecanicienNom()).isEqualTo("Benali");
            assertThat(chargeM1.nombreInterventionsActives()).isEqualTo(1);

            ChargeMecanicienDto chargeM2 = result.stream().filter(c -> c.mecanicienId().equals(2L)).findFirst().orElseThrow();
            assertThat(chargeM2.mecanicienNom()).isEqualTo("Sabri");
            assertThat(chargeM2.nombreInterventionsActives()).isZero();
        }

        @Test
        void calculerChargeMecaniciens_AucunMecanicien() {
            // Given
            when(mecanicienService.getAllMecaniciens(PageRequest.of(0, 10)))
                    .thenReturn(new PageImpl<>(Collections.emptyList()));
            when(interventionService.recupererListInterventions()).thenReturn(Collections.emptyList());

            // When
            List<ChargeMecanicienDto> result = dashboardService.calculerChargeMecaniciens();

            // Then
            assertThat(result).isEmpty();
        }
    }
}
