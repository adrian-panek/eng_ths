package com.pwr.project.entities.search;

import lombok.Data;

@Data
public class SearchCriteria {
    private String searchKey;
    private String searchValue;
    private Operator operator;
}
