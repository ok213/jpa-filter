package com.example.jpafilter.search;

import com.example.jpafilter.config.PersistenceTestConfig;
import com.example.jpafilter.model.Address;
import com.example.jpafilter.model.Passport;
import com.example.jpafilter.model.Person;
import com.example.jpafilter.repository.PersonQDSLRepository;
import com.example.jpafilter.search.querydsl.PersonPredicate;
import com.example.jpafilter.search.querydsl.PersonPredicatesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PersonPredicate.class})
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class PersonQDSLTest {

    @Autowired
    private PersonQDSLRepository repo;

    private Person john;
    private Person tom;
    private Person marta;

    @BeforeEach
    void setUp() {
        Passport passportJohn = new Passport("DD", "555555", LocalDate.of(2001, 12, 20), Period.ofYears(3));
        Passport passportTom = new Passport("EE", "667766", LocalDate.of(1991, 7, 15), Period.ofYears(5));
        Passport passportMarta = new Passport("FF", "777777", LocalDate.of(2011, 6, 25), Period.ofYears(10));

        john = new Person("John", "Doe", 35, LocalDate.of(1985, 11, 15));
        john.setPassport(passportJohn);
        john.setPrimaryAddress(new Address());
        john.setWorkingPlaces(new ArrayList<>());
        repo.save(john);

        tom = new Person("Tom", "Doe", 45, LocalDate.of(1975, 5, 3));
        tom.setPassport(passportTom);
        tom.setPrimaryAddress(new Address());
        tom.setWorkingPlaces(new ArrayList<>());
        repo.save(tom);

        marta = new Person("Marta", "Brown", 25, LocalDate.of(1995, 5, 3));
        marta.setPassport(passportMarta);
        marta.setPrimaryAddress(new Address());
        marta.setWorkingPlaces(new ArrayList<>());
        repo.save(marta);
    }

    @Test
    public void givenLast__whenGettingListOfPersons__thenCorrect() {
        PersonPredicatesBuilder builder = new PersonPredicatesBuilder().with("lastName", ":", "Doe");

        Iterable<Person> results = repo.findAll(builder.build());
        assertThat(results).contains(john, tom).doesNotContain(marta);
    }

    @Test
    public void givenFirstAndLastName_whenGettingListOfPersons_thenCorrect() {
        PersonPredicatesBuilder builder = new PersonPredicatesBuilder()
                .with("firstName", ":", "John").with("lastName", ":", "Doe");

        Iterable<Person> results = repo.findAll(builder.build());
        assertThat(results).containsOnly(john).doesNotContain(tom, marta);
    }

    @Test
    public void givenLastNameAndAge_whenGettingListOfPersons_thenCorrect() {
        PersonPredicatesBuilder builder = new PersonPredicatesBuilder()
                .with("lastName", ":", "Doe").with("age", ">", "40");

        Iterable<Person> results = repo.findAll(builder.build());
        assertThat(results).containsOnly(tom).doesNotContain(john, marta);
    }

    @Test
    public void givenPartialLast_whenGettingListOfPersons_thenCorrect() {
        PersonPredicatesBuilder builder = new PersonPredicatesBuilder().with("lastName", ":", "Bro");

        Iterable<Person> results = repo.findAll(builder.build());
        assertThat(results).containsOnly(marta).doesNotContain(tom, john);
    }

    @Test
    public void givenWrongFirstAndLast_whenGettingListOfPersons_thenCorrect() {
        PersonPredicatesBuilder builder = new PersonPredicatesBuilder()
                .with("firstName", ":", "Adam").with("lastName", ":", "Fox");

        Iterable<Person> results = repo.findAll(builder.build());
        assertThat(results).isEmpty();
    }
}
