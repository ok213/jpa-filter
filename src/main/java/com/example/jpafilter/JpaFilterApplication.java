package com.example.jpafilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableJpaRepositories(basePackages = {"com.example.jpafilter.repository", "com.example.jpafilter.my"})
public class  JpaFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaFilterApplication.class, args);
    }

}
