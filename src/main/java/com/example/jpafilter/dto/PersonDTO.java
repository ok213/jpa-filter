package com.example.jpafilter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String dob;
    private String passport;
    private String primaryAddress;
    private String workingPlaces;

}
