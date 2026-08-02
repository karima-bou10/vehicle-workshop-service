package com.workshop.vehicle_service.intervention.mapper;


import com.workshop.vehicle_service.intervention.dtos.HistoriqueInterventionResponse;
import com.workshop.vehicle_service.intervention.entity.HistoriqueIntervention;
import org.mapstruct.Mapper;

import java.util.List;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface HistoriqueInterventionMapper {

    @Mapping(target = "ancienStatut", expression = "java(historique.getAncienStatut().name())")
    @Mapping(target = "nouveauStatut", expression = "java(historique.getNouveauStatut().name())")
    HistoriqueInterventionResponse toResponse(HistoriqueIntervention historique);

    List<HistoriqueInterventionResponse> toResponseList(List<HistoriqueIntervention> historiques);
}