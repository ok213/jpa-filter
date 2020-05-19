package com.example.jpafilter.my;

import com.example.jpafilter.dto.PersonDTO;
import com.example.jpafilter.model.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface MyPersonService {

    List<PersonDTO> mySearchPerson(Specification<Person> spec, Sort sort);

}
