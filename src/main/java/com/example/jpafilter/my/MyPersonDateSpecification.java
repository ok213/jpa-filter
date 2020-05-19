package com.example.jpafilter.my;

import com.example.jpafilter.model.Person;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
public class MyPersonDateSpecification implements Specification<Person> {

    private MySearchCriteria criteria;

    public MySearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(final Root<Person> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        final LocalDate localDate = LocalDate.parse(criteria.getValue().toString(), formatter);

        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), localDate);
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), localDate);
            case GREATER_THAN:
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), localDate);
            case LESS_THAN:
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), localDate);
            default:
                return null;
        }
    }
}
