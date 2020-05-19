package com.example.jpafilter.my;

import com.example.jpafilter.model.Address;
import com.example.jpafilter.model.Company;
import com.example.jpafilter.model.Person;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@NoArgsConstructor
@AllArgsConstructor
public class MyPersonAddressSpecification implements Specification<Person> {

	private MySearchCriteria criteria;

	public MySearchCriteria getCriteria() {
		return criteria;
	}

	@Override
	public Predicate toPredicate(final Root<Person> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

		switch (criteria.getOperation()) {
			case EQUALITY:
				return builder.equal(root.get("primaryAddress").get(criteria.getKey()), criteria.getValue());
			case NEGATION:
				return builder.notEqual(root.get("primaryAddress").get(criteria.getKey()), criteria.getValue());
			case GREATER_THAN:
				return builder.greaterThanOrEqualTo(root.get("primaryAddress").get(criteria.getKey()), criteria.getValue().toString());
			case LESS_THAN:
				return builder.lessThanOrEqualTo(root.get("primaryAddress").get(criteria.getKey()), criteria.getValue().toString());
			case LIKE:
				return builder.like(root.get("primaryAddress").get(criteria.getKey()), criteria.getValue().toString());
			case STARTS_WITH:
				return builder.like(root.get("primaryAddress").get(criteria.getKey()), criteria.getValue() + "%");
			case ENDS_WITH:
				return builder.like(root.get("primaryAddress").get(criteria.getKey()), "%" + criteria.getValue());
			case CONTAINS:
				return builder.like(root.get("primaryAddress").get(criteria.getKey()), "%" + criteria.getValue() + "%");
			default:
				return null;
		}
	}

}
