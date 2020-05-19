package com.example.jpafilter.search.spec;

import com.example.jpafilter.model.Passport;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PassportSpecificationsBuilder {

    private final List<SearchCriteriaSpec> params;

    public PassportSpecificationsBuilder() {
        params = new ArrayList<SearchCriteriaSpec>();
    }

    public PassportSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteriaSpec(key, operation, value));
        return this;
    }

    public Specification<Passport> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(PassportSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specs.get(i))
                    : Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
