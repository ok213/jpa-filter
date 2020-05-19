package com.example.jpafilter.my;

import com.example.jpafilter.model.Company;
import com.example.jpafilter.model.Person;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@NoArgsConstructor
@AllArgsConstructor
public class MyPersonCompanySpecification implements Specification<Person> {

	private MySearchCriteria criteria;

	public MySearchCriteria getCriteria() {
		return criteria;
	}

	@Override
	public Predicate toPredicate(final Root<Person> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
		Join<Person, Company> companies = root.join("workingPlaces");

		switch (criteria.getOperation()) {
			case EQUALITY:
				return builder.equal(companies.get("name"), criteria.getValue());
			case NEGATION:
				return builder.notEqual(companies.get("name"), criteria.getValue());
			case GREATER_THAN:
				return builder.greaterThanOrEqualTo(companies.get("name"), criteria.getValue().toString());
			case LESS_THAN:
				return builder.lessThanOrEqualTo(companies.get("name"), criteria.getValue().toString());
			case LIKE:
				return builder.like(companies.get("name"), criteria.getValue().toString());
			case STARTS_WITH:
				return builder.like(companies.get("name"), criteria.getValue() + "%");
			case ENDS_WITH:
				return builder.like(companies.get("name"), "%" + criteria.getValue());
			case CONTAINS:
				return builder.like(companies.get("name"), "%" + criteria.getValue() + "%");
			default:
				return null;
		}
	}

}
