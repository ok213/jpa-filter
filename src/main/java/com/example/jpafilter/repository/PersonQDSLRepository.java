package com.example.jpafilter.repository;

import com.example.jpafilter.model.Person;
import com.example.jpafilter.model.QPerson;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonQDSLRepository extends JpaRepository<Person, Long>,
        QuerydslPredicateExecutor<Person>, QuerydslBinderCustomizer<QPerson> {

    @Override
    default public void customize(QuerydslBindings bindings, QPerson root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
//        bindings.excluding(root.firstName);
    }
}
