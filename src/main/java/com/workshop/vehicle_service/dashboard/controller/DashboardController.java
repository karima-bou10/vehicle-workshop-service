package com.workshop.vehicle_service.dashboard.controller;

import com.workshop.vehicle_service.dashboard.dto.ChargeMecanicienDto;
import com.workshop.vehicle_service.dashboard.dto.DashboardResumeDto;
import com.workshop.vehicle_service.dashboard.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/resume")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ResponseEntity<DashboardResumeDto> getResume() {
        return ResponseEntity.ok(dashboardService.calculerResumeGlobal());
    }

    @GetMapping("/charge-mecaniciens")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ResponseEntity<List<ChargeMecanicienDto>> getChargeMecaniciens() {
        return ResponseEntity.ok(dashboardService.calculerChargeMecaniciens());
    }
}


