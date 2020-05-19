package com.example.jpafilter.service;

import com.example.jpafilter.dto.PassportDTO;
import com.example.jpafilter.dto.PersonDTO;
import com.example.jpafilter.model.Passport;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PassportService {

    List<PassportDTO> searchPassport(Specification<Passport> specification);

}
