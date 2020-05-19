package com.example.jpafilter.service;

import com.example.jpafilter.dto.PassportDTO;
import com.example.jpafilter.model.Passport;
import com.example.jpafilter.repository.PassportRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;

    @Override
    public List<PassportDTO> searchPassport(Specification<Passport> specification) {

        List<Passport> passports = passportRepository.findAll(specification);

        List<PassportDTO> dto = new ArrayList<>();
        passports.forEach(passport -> {
            PassportDTO pdto = new PassportDTO();
            pdto.setId(passport.getId());
            pdto.setSeries(passport.getSeries());
            pdto.setNo(passport.getNo());
            pdto.setIssueDate(passport.getIssueDate().toString());
            pdto.setOwner(passport.getOwner().getFirstName() + " " + passport.getOwner().getLastName());
            dto.add(pdto);
        });

        return dto;
    }
}
