package com.example.jpafilter.dao;

import com.example.jpafilter.model.Person;
import com.example.jpafilter.search.simple.SearchCriteria;
import com.example.jpafilter.search.simple.PersonSearchQueryCriteriaConsumer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class PersonDAOImpl implements PersonDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Person> searchPerson(final List<SearchCriteria> params) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Person> query = builder.createQuery(Person.class);
        final Root root = query.from(Person.class);

        Predicate predicate = builder.conjunction();
        PersonSearchQueryCriteriaConsumer searchConsumer = new PersonSearchQueryCriteriaConsumer(predicate, builder, root);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        List<Person> persons = entityManager.createQuery(query).getResultList();

        return persons;
    }

    @Override
    public void save(final Person entity) {
        entityManager.persist(entity);
    }

}
