package com.example.jpafilter.search.spec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteriaSpec {

    private String key;
    private String operation;
    private Object value;
    private boolean orPredicate;

    public SearchCriteriaSpec(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

//    public boolean isOrPredicate() {
//        return orPredicate;
//    }
//
//    public void setOrPredicate(boolean orPredicate) {
//        this.orPredicate = orPredicate;
//    }

}
