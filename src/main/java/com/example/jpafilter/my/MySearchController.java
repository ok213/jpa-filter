package com.example.jpafilter.my;

import com.example.jpafilter.dto.PersonDTO;
import com.example.jpafilter.model.Person;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@AllArgsConstructor
@RequestMapping("/my")
public class MySearchController {

    private final MyPersonService myPersonService;

    @GetMapping("/persons")
    public List<PersonDTO> myFindAllBySpecification(@RequestParam(value = "search") String search, Sort sort) {

        MyPersonSpecificationsBuilder builder = new MyPersonSpecificationsBuilder();
        String operationSetExper = Joiner.on("|").join(MySearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }

        Specification<Person> spec = builder.build();

        return myPersonService.mySearchPerson(spec, sort);
    }

}





