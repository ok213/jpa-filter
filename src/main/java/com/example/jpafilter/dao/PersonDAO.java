package com.example.jpafilter.dao;

import com.example.jpafilter.model.Person;
import com.example.jpafilter.search.simple.SearchCriteria;

import java.util.List;

public interface PersonDAO {

    List<Person> searchPerson(List<SearchCriteria> params);

    void save(Person entity);
}
