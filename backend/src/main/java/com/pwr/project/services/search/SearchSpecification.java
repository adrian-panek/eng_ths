package com.pwr.project.services.search;

import com.pwr.project.entities.Notice;
import com.pwr.project.entities.search.SearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SearchSpecification {
    public static Specification<Notice> createSpecification(List<SearchCriteria> searchCriteria){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = searchCriteria.stream().map(criteria -> criteria.getOperator()
                    .buildCriteria(root, query, criteriaBuilder, criteria.getSearchKey(), criteria.getSearchValue())).toList();
                    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
