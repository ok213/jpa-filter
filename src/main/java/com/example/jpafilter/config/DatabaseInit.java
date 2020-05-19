package com.example.jpafilter.config;

import com.example.jpafilter.model.Address;
import com.example.jpafilter.model.Company;
import com.example.jpafilter.model.Passport;
import com.example.jpafilter.model.Person;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseInit implements CommandLineRunner {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void run(String... args) throws Exception {

        Passport p1 = new Passport("AA", "123321", LocalDate.of(2011, 2, 20), Period.ofYears(10));
        Passport p2 = new Passport("BB", "234444", LocalDate.of(2006, 7, 15), Period.ofYears(10));
        Passport p3 = new Passport("CC", "567898", LocalDate.of(1997, 1, 25), Period.ofYears(10));

        Address a1 = new Address("Piter", "Main street", "2");
        Address a2 = new Address("Moscow", "Red square", "1");

        Person person1 = new Person("John", "First", 25, LocalDate.of(1995, 1, 20));
        person1.setPrimaryAddress(a1);
        person1.setPassport(p1);
        p1.setOwner(person1);

        Person person2 = new Person("Marta", "First", 30, LocalDate.of(1990, 5, 9));
        person2.setPrimaryAddress(a1);
        person2.setPassport(p2);
        p2.setOwner(person2);

        Person person3 = new Person("Bill", "Second", 40, LocalDate.of(1980, 12, 12));
        person3.setPrimaryAddress(a2);
        person3.setPassport(p3);
        p3.setOwner(person3);

        Company c1 = new Company("Home");
        Company c2 = new Company("Acme");
        Company c3 = new Company("Udalenka");

        Collection<Company> col1 = new ArrayList<>();
        col1.add(c1);
        col1.add(c3);

        Collection<Company> col2 = new ArrayList<>();
        col2.add(c2);
        col2.add(c3);

        person1.setWorkingPlaces(Collections.singletonList(c1));
        person2.setWorkingPlaces(col2);
        person3.setWorkingPlaces(Collections.singletonList(c3));

        List<Person> tenants = new ArrayList<>();
        tenants.add(person1);
        tenants.add(person2);
        a1.setTenants(tenants);

        a2.setTenants(Collections.singletonList(person3));

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.merge(person1);
        em.merge(person2);
        em.merge(person3);
        em.getTransaction().commit();
        em.close();

    }
}
