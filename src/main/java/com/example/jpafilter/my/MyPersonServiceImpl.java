package com.example.jpafilter.my;

import com.example.jpafilter.dto.PersonDTO;
import com.example.jpafilter.model.Company;
import com.example.jpafilter.model.Person;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MyPersonServiceImpl implements MyPersonService {

    private final MyPersonRepository repo;

    @Override
    public List<PersonDTO> mySearchPerson(Specification<Person> spec, Sort sort) {

        List<Person> persons = repo.findAll(spec, sort);
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
