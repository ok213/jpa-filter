package com.example.jpafilter.search.specadvanced;

import com.example.jpafilter.model.Person;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class PersonSpecificationsBuilder {

    private final List<SearchCriteriaSpecAdv> params;

    public PersonSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public PersonSpecificationsBuilder with(String key, String operation, Object value, String prefix, String suffix) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SearchCriteriaSpecAdv(key, op, value));
        }
        return this;
    }

    public Specification<Person> build() {
        if (params.size() == 0) {
            return null;
        }

        Specification result = new PersonSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new PersonSpecification(params.get(i)))
                    : Specification.where(result).and(new PersonSpecification(params.get(i)));
        }

        return result;
    }

}
