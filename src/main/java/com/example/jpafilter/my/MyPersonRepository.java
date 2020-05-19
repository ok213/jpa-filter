package com.example.jpafilter.my;

import com.example.jpafilter.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
}
