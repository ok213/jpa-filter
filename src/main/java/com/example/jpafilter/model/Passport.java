package com.example.jpafilter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passports")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String series;
    private String no;
    private LocalDate issueDate;
    private Period validity;

    @OneToOne(mappedBy = "passport")
    private Person owner;

    public Passport(String series, String no, LocalDate issueDate, Period validity) {
        this.series = series;
        this.no = no;
        this.issueDate = issueDate;
        this.validity = validity;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "series='" + series + '\'' +
                ", no='" + no + '\'' +
                ", issueDate=" + issueDate +
                ", validity=" + validity +
                ", owner=" + owner.getLastName() +
                '}';
    }

}
