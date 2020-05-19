package com.example.jpafilter.search;

import com.example.jpafilter.config.PersistenceTestConfig;
import com.example.jpafilter.model.Address;
import com.example.jpafilter.model.Passport;
import com.example.jpafilter.model.Person;
import com.example.jpafilter.repository.PersonRepository;
import com.example.jpafilter.search.specadvanced.PersonSpecification;
import com.example.jpafilter.search.specadvanced.SearchCriteriaSpecAdv;
import com.example.jpafilter.search.specadvanced.SearchOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PersonSpecification.class})
@ContextConfiguration(classes = {PersistenceTestConfig.class})
class PersonSpecificationTest {

    @Autowired
    private PersonRepository repository;

    private Person john;
    private Person tom;
    private Person marta;

    @BeforeEach
    void setUp() {
        Passport passportJohn = new Passport("DD", "555555", LocalDate.of(2001, 12, 20), Period.ofYears(10));
        Passport passportTom = new Passport("EE", "667766", LocalDate.of(1991, 7, 15), Period.ofYears(10));
        Passport passportMarta = new Passport("FF", "777777", LocalDate.of(2011, 6, 25), Period.ofYears(10));

        john = new Person("John", "Doe", 35, LocalDate.of(1985, 11, 15));
        john.setPassport(passportJohn);
        john.setPrimaryAddress(new Address());
        john.setWorkingPlaces(new ArrayList<>());

        tom = new Person("Tom", "Doe", 45, LocalDate.of(1975, 5, 3));
        tom.setPassport(passportTom);
        tom.setPrimaryAddress(new Address());
        tom.setWorkingPlaces(new ArrayList<>());

        marta = new Person("Marta", "Brown", 25, LocalDate.of(1995, 5, 3));
        marta.setPassport(passportMarta);
        marta.setPrimaryAddress(new Address());
        marta.setWorkingPlaces(new ArrayList<>());

        passportJohn.setOwner(john);
        passportTom.setOwner(tom);
        passportMarta.setOwner(marta);

        repository.save(john);
        repository.save(tom);
        repository.save(marta);
    }

    @Test
    public void givenFirstAndLastName_whenGettingListOfPersons_thenCorrect() {
        PersonSpecification spec = new PersonSpecification(
                new SearchCriteriaSpecAdv("firstName", SearchOperation.EQUALITY, "John"));
        PersonSpecification spec1 = new PersonSpecification(
                new SearchCriteriaSpecAdv("lastName", SearchOperation.EQUALITY, "Doe"));
        List<Person> results = repository.findAll(Specification.where(spec).and(spec1));

        assertThat(results).containsOnly(john).doesNotContain(tom, marta);
    }

    @Test
    public void givenFirstNameInverse_whenGettingListOfPersons_thenCorrect() {
        PersonSpecification spec = new PersonSpecification(
                new SearchCriteriaSpecAdv("firstName", SearchOperation.NEGATION, "John"));
        List<Person> results = repository.findAll(Specification.where(spec));

        assertThat(results).contains(tom, marta).doesNotContain(john);
    }

    @Test
    public void givenMinAge_whenGettingListOfPersons_thenCorrect() {
        PersonSpecification spec = new PersonSpecification(
                new SearchCriteriaSpecAdv("age", SearchOperation.GREATER_THAN, "30"));
        List<Person> results = repository.findAll(Specification.where(spec));

        assertThat(results).contains(john, tom).doesNotContain(marta);
    }

    @Test
    public void givenFirstNamePrefix_whenGettingListOfPersons_thenCorrect() {
        PersonSpecification spec = new PersonSpecification(
                new SearchCriteriaSpecAdv("firstName", SearchOperation.STARTS_WITH, "To"));
        List<Person> results = repository.findAll(spec);

        assertThat(results).containsOnly(tom);
    }

    @Test
    public void givenFirstNameSuffix_whenGettingListOfPersons_thenCorrect() {
        PersonSpecification spec = new PersonSpecification(
                new SearchCriteriaSpecAdv("lastName", SearchOperation.ENDS_WITH, "n"));
        List<Person> results = repository.findAll(spec);

        assertThat(results).containsOnly(marta);
    }

    @Test
    public void givenFirstNameSubstring_whenGettingListOfPersons_thenCorrect() {
        PersonSpecification spec = new PersonSpecification(
                new SearchCriteriaSpecAdv("lastName", SearchOperation.CONTAINS, "ow"));
        List<Person> results = repository.findAll(spec);

        assertThat(results).containsOnly(marta);
    }

    @Test
    public void givenAgeRange_whenGettingListOfPersons_thenCorrect() {
        PersonSpecification spec = new PersonSpecification(
                new SearchCriteriaSpecAdv("age", SearchOperation.GREATER_THAN, "20"));
        PersonSpecification spec1 = new PersonSpecification(
                new SearchCriteriaSpecAdv("age", SearchOperation.LESS_THAN, "26"));
        List<Person> results = repository.findAll(Specification.where(spec).and(spec1));

        assertThat(results).contains(marta).doesNotContain(john, tom);
    }
}
