package com.workshop.vehicle_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {
		"com.workshop.vehicle_service.config",
		"com.workshop.vehicle_service.mecanicien",
		"com.workshop.vehicle_service.vehicule"
})
public class VehicleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleServiceApplication.class, args);
	}

}