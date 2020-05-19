package com.example.jpafilter.dao;

import com.example.jpafilter.config.PersistenceTestConfig;
import com.example.jpafilter.model.Address;
import com.example.jpafilter.model.Passport;
import com.example.jpafilter.model.Person;
import com.example.jpafilter.search.simple.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PersonDAOImpl.class)
@ContextConfiguration(classes = {PersistenceTestConfig.class})
class PersonDAOImplTest {

    @Autowired
    private PersonDAO personDAO;
    private Person john;
    private Person tom;

    @BeforeEach
    void setUp() {
        Passport passportJohn = new Passport("DD", "555555", LocalDate.of(2001, 12, 20), Period.ofYears(10));
        Passport passportTom = new Passport("EE", "667766", LocalDate.of(1991, 7, 15), Period.ofYears(10));

        john = new Person("john", "doe", 35, LocalDate.of(2017, 11, 15));
        john.setPassport(passportJohn);
        john.setPrimaryAddress(new Address());
        john.setWorkingPlaces(new ArrayList<>());
        passportJohn.setOwner(john);
        personDAO.save(john);

        tom = new Person("tom", "doe", 45, LocalDate.of(2019, 5, 3));
        tom.setPassport(passportTom);
        tom.setPrimaryAddress(new Address());
        tom.setWorkingPlaces(new ArrayList<>());
        passportTom.setOwner(tom);
        personDAO.save(tom);
    }

    @Test
    public void givenFirstAndLastName_whenGettingListOfPersons_thenCorrect() {
        final List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("firstName", ":", "john"));
        params.add(new SearchCriteria("lastName", ":", "doe"));

        final List<Person> results = personDAO.searchPerson(params);

        assertThat(results).containsOnly(john);
    }

    @Test
    public void givenLast_whenGettingListOfPersons_thenCorrect() {
        final List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("lastName", ":", "doe"));

        final List<Person> results = personDAO.searchPerson(params);

        assertThat(results).contains(john, tom);
    }

    @Test
    public void givenWrongFirstAndLast_whenGettingListOfPersons_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("firstName", ":", "Adam"));
        params.add(new SearchCriteria("lastName", ":", "Fox"));

        List<Person> results = personDAO.searchPerson(params);

        assertThat(results).doesNotContain(john, tom);
    }

    @Test
    public void givenPartialFirst_whenGettingListOfPersons_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("firstName", ":", "jo"));

        List<Person> results = personDAO.searchPerson(params);

        assertThat(results).containsOnly(john);
    }

    @Test
    public void givenLastAndAge_whenGettingListOfPersons_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("lastName", ":", "doe"));
        params.add(new SearchCriteria("age", ">", 40));

        List<Person> results = personDAO.searchPerson(params);

        assertThat(results).containsOnly(tom);
    }

    @Test
    public void givenLastAndDob_whenGettingListOfPersons_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("lastName", ":", "doe"));
        params.add(new SearchCriteria("dob", ":", LocalDate.of(2019, 5, 3)));

        List<Person> results = personDAO.searchPerson(params);

        assertThat(results).containsOnly(tom);
    }
}