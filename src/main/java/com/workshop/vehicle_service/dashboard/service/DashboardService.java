package com.workshop.vehicle_service.dashboard.service;

import com.workshop.vehicle_service.dashboard.dto.ChargeMecanicienDto;
import com.workshop.vehicle_service.dashboard.dto.DashboardResumeDto;

import java.util.List;

public interface DashboardService {
    DashboardResumeDto calculerResumeGlobal();

    List<ChargeMecanicienDto> calculerChargeMecaniciens();
}
