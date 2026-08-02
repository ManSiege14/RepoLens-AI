package com.repolens.repository.specification;

import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.service.RepositoryFilter;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class RepositorySpecification {

    private RepositorySpecification() {
    }

    public static Specification<GitRepository> withFilter(
            RepositoryFilter filter
    ) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.query() != null && !filter.query().isBlank()) {

                String search = "%" + filter.query().toLowerCase() + "%";

                predicates.add(
                        criteriaBuilder.or(

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("name")),
                                        search
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("owner")),
                                        search
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("fullName")),
                                        search
                                )
                        )
                );
            }

            if (filter.owner() != null && !filter.owner().isBlank()) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("owner")),
                                filter.owner().toLowerCase()
                        )
                );
            }

            if (filter.language() != null && !filter.language().isBlank()) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("primaryLanguage")),
                                filter.language().toLowerCase()
                        )
                );
            }

            if (filter.visibility() != null && !filter.visibility().isBlank()) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("visibility")),
                                filter.visibility().toLowerCase()
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}