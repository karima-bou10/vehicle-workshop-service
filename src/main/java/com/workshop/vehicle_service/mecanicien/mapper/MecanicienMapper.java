package com.workshop.vehicle_service.mecanicien.mapper;

import com.workshop.vehicle_service.mecanicien.dto.CreateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.dto.MecanicienResponse;
import com.workshop.vehicle_service.mecanicien.dto.UpdateMecanicienRequest;
import com.workshop.vehicle_service.mecanicien.entity.Mecanicien;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MecanicienMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "interventions", ignore = true)
    @Mapping(target = "disponible", constant = "true") // Disponible par défaut
    Mecanicien toEntity(CreateMecanicienRequest request);

    MecanicienResponse toResponse(Mecanicien entity);

    List<MecanicienResponse> toResponseList(List<Mecanicien> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "interventions", ignore = true)
    void updateEntityFromRequest(UpdateMecanicienRequest request, @MappingTarget Mecanicien entity);
}
