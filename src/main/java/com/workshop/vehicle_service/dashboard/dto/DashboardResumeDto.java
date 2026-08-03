package com.workshop.vehicle_service.dashboard.dto;

import java.util.List;

public record DashboardResumeDto(
        long recuesAujourdhui,
        long enDiagnostic,
        long enReparation,
        long terminees,
        long retardsRestitution,
        List<StatutCompteurDto> repartitionStatuts
) {}
