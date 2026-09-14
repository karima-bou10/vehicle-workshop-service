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
    @Mapping(source = "vehicule.immatriculationFictive", target = "immatriculationVehicule")
    @Mapping(source = "vehicule.marque", target = "marque")
    @Mapping(source = "vehicule.modele", target = "modele")

    @Mapping(source = "mecanicien.id", target = "mecanicienId")
    @Mapping(source = "mecanicien.nom", target = "nomMecanicien")
    @Mapping(source = "mecanicien.prenom", target = "prenomMecanicien")

    InterventionResponse toResponse(Intervention intervention);


    List<InterventionResponse> toResponseList(List<Intervention> list);

    void updateEntity(InterventionUpdateRequest request,
                      @MappingTarget Intervention intervention);
}
