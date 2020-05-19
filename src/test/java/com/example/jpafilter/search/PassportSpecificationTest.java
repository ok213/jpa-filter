package com.example.jpafilter.search;

import com.example.jpafilter.config.PersistenceTestConfig;
import com.example.jpafilter.dao.PersonDAO;
import com.example.jpafilter.model.Address;
import com.example.jpafilter.model.Passport;
import com.example.jpafilter.model.Person;
import com.example.jpafilter.repository.PassportRepository;
import com.example.jpafilter.search.spec.PassportSpecification;
import com.example.jpafilter.search.spec.SearchCriteriaSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
@SpringBootTest(classes = {PassportSpecification.class})
@ContextConfiguration(classes = {PersistenceTestConfig.class})
class PassportSpecificationTest {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private PassportRepository passportRepository;

    private Passport passportJohn;
    private Passport passportTom;
    private Passport passportMarta;

    @BeforeEach
    void setUp() {
        passportJohn = new Passport("DD", "555555", LocalDate.of(2001, 12, 20), Period.ofYears(10));
        passportTom = new Passport("EE", "667766", LocalDate.of(1991, 7, 15), Period.ofYears(10));
        passportMarta = new Passport("FF", "777777", LocalDate.of(2011, 6, 25), Period.ofYears(10));

        Person john = new Person("John", "Doe", 35, LocalDate.of(1985, 11, 15));
        john.setPassport(passportJohn);
        john.setPrimaryAddress(new Address());
        john.setWorkingPlaces(new ArrayList<>());

        Person tom = new Person("Tom", "Doe", 45, LocalDate.of(1975, 5, 3));
        tom.setPassport(passportTom);
        tom.setPrimaryAddress(new Address());
        tom.setWorkingPlaces(new ArrayList<>());

        Person marta = new Person("Marta", "Brown", 25, LocalDate.of(1995, 5, 3));
        marta.setPassport(passportMarta);
        marta.setPrimaryAddress(new Address());
        marta.setWorkingPlaces(new ArrayList<>());

        passportJohn.setOwner(john);
        passportTom.setOwner(tom);
        passportMarta.setOwner(marta);

        personDAO.save(john);
        personDAO.save(tom);
        personDAO.save(marta);
    }

    @Test
    public void givenSeries_whenGettingListOfPassports_thenCorrect() {
        PassportSpecification spec = new PassportSpecification(new SearchCriteriaSpec("series", ":", "D"));
        List<Passport> results = passportRepository.findAll(spec);
        assertThat(results).containsOnly(passportJohn).doesNotContain(passportMarta, passportTom);
    }

    @Test
    public void givenNo_whenGettingListOfPassports_thenCorrect() {
        PassportSpecification spec = new PassportSpecification(new SearchCriteriaSpec("no", ":", "5"));
        List<Passport> results = passportRepository.findAll(spec);
        assertThat(results).contains(passportJohn).doesNotContain(passportTom, passportMarta);
    }

    @Test
    public void givenSeriesAndNo_whenGettingListOfPersons_thenCorrect() {
        PassportSpecification spec1 = new PassportSpecification(new SearchCriteriaSpec("series", "<", "FF"));
        PassportSpecification spec2 = new PassportSpecification(new SearchCriteriaSpec("no", ":", "77"));

        List<Passport> results = passportRepository.findAll(Specification.where(spec1).and(spec2));

        assertThat(results).contains(passportTom, passportMarta).doesNotContain(passportJohn);
    }

    @Test
    public void givenNoOrIssueDate_whenGettingListOfPassports_thenCorrect() {
        PassportSpecification spec1 = new PassportSpecification(new SearchCriteriaSpec("no", ":", "55"));
        PassportSpecification spec2 = new PassportSpecification(new SearchCriteriaSpec("issueDate", ":", LocalDate.of(2011, 6, 25)));

        List<Passport> results = passportRepository.findAll(Specification.where(spec1).or(spec2));

        assertThat(results).contains(passportJohn, passportMarta).doesNotContain(passportTom);
    }

    @Test
    public void givenWrongNoOrSeries_whenGettingListOfPassports_thenCorrect() {
        PassportSpecification spec1 = new PassportSpecification(new SearchCriteriaSpec("no", ":", "000000"));
        PassportSpecification spec2 = new PassportSpecification(new SearchCriteriaSpec("series", ">", "J"));

        List<Passport> results = passportRepository.findAll(Specification.where(spec1).or(spec2));

        assertThat(results).doesNotContain(passportTom, passportJohn, passportMarta);
    }

}
