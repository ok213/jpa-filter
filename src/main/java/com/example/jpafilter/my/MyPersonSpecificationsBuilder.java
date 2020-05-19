package com.example.jpafilter.my;

import com.example.jpafilter.model.Person;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class MyPersonSpecificationsBuilder {

    private final List<MySearchCriteria> params;

    public MyPersonSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public MyPersonSpecificationsBuilder with(String key, String operation, Object value, String prefix, String suffix) {

        MySearchOperation op = MySearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == MySearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = MySearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = MySearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = MySearchOperation.STARTS_WITH;
                }
            }
            params.add(new MySearchCriteria(key, op, value));
        }
        return this;
    }

    public Specification<Person> build() {
        if (params.size() == 0) {
            return null;
        }

        Specification result = getSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(getSpecification(params.get(i)))
                    : Specification.where(result).and(getSpecification(params.get(i)));
        }

        return result;
    }

    private Specification<Person> getSpecification(MySearchCriteria criteria) {
        switch (criteria.getKey()){
            case "city":
            case "street":
            case "building":
                return new MyPersonAddressSpecification(criteria);
            case "company":
                return new MyPersonCompanySpecification(criteria);
            case "dob":
                return new MyPersonDateSpecification(criteria);
            default:
                return new MyPersonSpecification(criteria);
        }
    }

}
