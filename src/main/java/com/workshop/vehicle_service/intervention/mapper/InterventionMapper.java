package com.workshop.vehicle_service.intervention.mapper;


import com.workshop.vehicle_service.intervention.dtos.InterventionCreationRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionRequest;
import com.workshop.vehicle_service.intervention.dtos.InterventionResponse;
import com.workshop.vehicle_service.intervention.dtos.InterventionUpdateRequest;
import com.workshop.vehicle_service.intervention.entity.Intervention;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InterventionMapper {

    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "mecanicien", ignore = true)
    @Mapping(target = "historiqueInterventionList", ignore = true)
    Intervention toEntity(InterventionCreationRequest request);

    @Mapping(source = "vehicule.id", target = "vehiculeId")
    @Mapping(source = "mecanicien.id", target = "mecanicienId")
    @Mapping(source = "mecanicien.nom", target = "nomMecanicien")
    InterventionResponse toResponse(Intervention intervention);

    List<InterventionResponse> toResponseList(List<Intervention> list);

    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "mecanicien", ignore = true)
    @Mapping(target = "historiqueInterventionList", ignore = true)
    void updateEntity(InterventionUpdateRequest request,
                      @MappingTarget Intervention intervention);
}
