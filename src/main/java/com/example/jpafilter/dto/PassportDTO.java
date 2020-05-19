package com.example.jpafilter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassportDTO {

    private Long id;
    private String series;
    private String no;
    private String issueDate;
    private String validity;
    private String owner;

}
