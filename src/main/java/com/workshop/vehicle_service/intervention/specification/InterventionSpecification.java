package com.workshop.vehicle_service.intervention.specification;

import com.workshop.vehicle_service.intervention.entity.Intervention;
import com.workshop.vehicle_service.intervention.enums.Priorite;
import com.workshop.vehicle_service.intervention.enums.StatutIntervention;
import com.workshop.vehicle_service.intervention.enums.TypeIntervention;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InterventionSpecification {

    public static Specification<Intervention> withFilters(
            String reference,
            String immatriculation,
            StatutIntervention statut,
            Priorite priorite,
            TypeIntervention typeIntervention,
            Long vehiculeId,
            Long mecanicienId,
            boolean includeArchived
    ) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Exclure les interventions archivées
            if (!includeArchived) {
                predicates.add(
                        criteriaBuilder.isFalse(root.get("deleted"))
                );
            }

            // Recherche par référence
            if (reference != null && !reference.isBlank()) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(
                                        root.get("reference")
                                ),
                                reference.trim().toLowerCase()
                        )
                );
            }

            // Recherche par immatriculation
            if (immatriculation != null && !immatriculation.isBlank()) {

                Join<Intervention, ?> vehicule =
                        root.join("vehicule", JoinType.LEFT);

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        vehicule.get("immatriculationFictive")
                                ),
                                "%" + immatriculation.trim().toLowerCase() + "%"
                        )
                );
            }

            // Recherche par statut
            if (statut != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("statut"),
                                statut
                        )
                );
            }

            // Recherche par priorité
            if (priorite != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("priorite"),
                                priorite
                        )
                );
            }

            // Recherche par type
            if (typeIntervention != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("typeIntervention"),
                                typeIntervention
                        )
                );
            }

            // Recherche par véhicule
            if (vehiculeId != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("vehicule").get("id"),
                                vehiculeId
                        )
                );
            }

            // Recherche par mécanicien
            if (mecanicienId != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("mecanicien").get("id"),
                                mecanicienId
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}