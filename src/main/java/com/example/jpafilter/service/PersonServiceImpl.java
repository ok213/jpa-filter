package com.example.jpafilter.service;

import com.example.jpafilter.dao.PersonDAO;
import com.example.jpafilter.dto.PersonDTO;
import com.example.jpafilter.model.Company;
import com.example.jpafilter.model.Person;
import com.example.jpafilter.repository.PersonQDSLRepository;
import com.example.jpafilter.repository.PersonRepository;
import com.example.jpafilter.search.simple.SearchCriteria;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonDAO personDAO;
    private final PersonQDSLRepository repoQDSL;
    private final PersonRepository personRepository;

    @Override
    public List<PersonDTO> searchPersonSpec(Specification<Person> spec) {
        List<Person> persons = personRepository.findAll(spec);
        return getPersonDTO(persons);
    }

    @Override
    public List<PersonDTO> searchPerson(List<SearchCriteria> params) {
        List<Person> persons = personDAO.searchPerson(params);
        return getPersonDTO(persons);
    }

    @Override
    public List<PersonDTO> searchPersonQDSL(BooleanExpression exp) {
        Iterable<Person> persons = repoQDSL.findAll(exp);
        return getPersonDTO(persons);
    }

    private List<PersonDTO> getPersonDTO(Iterable<Person> persons) {
        List<PersonDTO> dto = new ArrayList<>();
        persons.forEach(person -> {
            PersonDTO pdto = new PersonDTO();
            pdto.setId(person.getId());
            pdto.setFirstName(person.getFirstName());
            pdto.setLastName(person.getLastName());
            pdto.setAge(person.getAge());
            pdto.setDob(person.getDob().toString());
            pdto.setPassport(person.getPassport().getSeries() + " " + person.getPassport().getNo());
            pdto.setPrimaryAddress(person.getPrimaryAddress().getCity()
                    + ", " + person.getPrimaryAddress().getStreet()
                    + ", " + person.getPrimaryAddress().getBuilding()
            );
            pdto.setWorkingPlaces(person.getWorkingPlaces().stream().map(Company::getName).collect(Collectors.joining(",")));
            dto.add(pdto);
        });
        return dto;
    }

}
