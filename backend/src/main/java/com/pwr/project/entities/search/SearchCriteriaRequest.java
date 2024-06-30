package com.pwr.project.entities.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchCriteriaRequest {
    List<SearchCriteria> searchCriteria;
}
