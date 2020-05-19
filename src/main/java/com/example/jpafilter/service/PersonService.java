package com.example.jpafilter.service;

import com.example.jpafilter.dto.PersonDTO;
import com.example.jpafilter.model.Person;
import com.example.jpafilter.search.simple.SearchCriteria;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PersonService {

    List<PersonDTO> searchPerson(List<SearchCriteria> params);
    List<PersonDTO> searchPersonQDSL(BooleanExpression exp);
    List<PersonDTO> searchPersonSpec(Specification<Person> spec);

}
