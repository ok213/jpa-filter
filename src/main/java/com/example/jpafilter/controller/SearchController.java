package com.example.jpafilter.controller;

import com.example.jpafilter.dto.PassportDTO;
import com.example.jpafilter.dto.PersonDTO;
import com.example.jpafilter.model.Passport;
import com.example.jpafilter.model.Person;
import com.example.jpafilter.search.querydsl.PersonPredicatesBuilder;
import com.example.jpafilter.search.simple.SearchCriteria;
import com.example.jpafilter.search.spec.PassportSpecificationsBuilder;
import com.example.jpafilter.search.specadvanced.PersonSpecificationsBuilder;
import com.example.jpafilter.search.specadvanced.SearchOperation;
import com.example.jpafilter.service.PassportService;
import com.example.jpafilter.service.PersonService;
import com.google.common.base.Joiner;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final PersonService personService;
    private final PassportService passportService;

    // http://localhost:8080/api/spec/persons?search=firstName:Jo*,age>20
    @GetMapping("/spec/persons")
    public List<PersonDTO> findAllBySpecification(@RequestParam(value = "search") String search) {
        PersonSpecificationsBuilder builder = new PersonSpecificationsBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }

        Specification<Person> spec = builder.build();
        return personService.searchPersonSpec(spec);
    }

//     http://localhost:8080/api/qdsl/person?search=lastName:First,age>30
    @GetMapping("/qdsl/person")
    public Iterable<PersonDTO> searchPersonQDSL(@RequestParam(value = "search") String search) {
        PersonPredicatesBuilder builder = new PersonPredicatesBuilder();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }

        BooleanExpression exp = builder.build();
        return personService.searchPersonQDSL(exp);
    }


    // http://localhost:8080/api/person?search=lastName:John,age>20
    @GetMapping("/person")
    public List<PersonDTO> searchPerson(@RequestParam(value = "search", required = false) String search) {
        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        return personService.searchPerson(params);
    }

    // http://localhost:8080/api/passport?search=series:CC,no:8
    @GetMapping("/passport")
    public List<PassportDTO> search(@RequestParam(value = "search") String search) {
        PassportSpecificationsBuilder builder = new PassportSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Passport> spec = builder.build();
        return passportService.searchPassport(spec);
    }


}
