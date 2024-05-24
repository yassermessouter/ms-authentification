package com.alibou.security.company;

import com.alibou.security.auth.AuthenticationService;
import com.alibou.security.token.Token;
import com.alibou.security.token.TokenRepository;
import com.alibou.security.user.StateType;
import com.alibou.security.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationService authenticationService;

    public List<CompanyResponseDto> findAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .filter(company -> !company.getName().equals("Supplier")) // Exclude the company with name "Supplier"
                .map(company -> new CompanyResponseDto(company.getName(), company.getAddress(), company.getStateType()))
                .collect(Collectors.toList());
    }


    public CompanyDetailsDto findCompanyByname(String name) {
        Company company= companyRepository.findByName(name).orElseThrow();
        List<String> fileUrls=new ArrayList<>();
        for (FileMetadata fileMetadata: company.getFileUrls()){
            fileUrls.add(fileMetadata.getFileUrl());
        }

        return CompanyDetailsDto.builder()
                .name(company.getName())
                .address(company.getAddress())
                .fileUrls(fileUrls)
                .stateType(company.getStateType())
                .categories(company.getCategories())
                .build();
    }

    public String updateCompanyDetails(CompanyUpdatedDto companyUpdatedDto) {
        Company company=companyRepository.findByName(companyUpdatedDto.getCompanyName()).orElseThrow();
        Boolean oldState=company.getStateType()==StateType.INACTIVE;
        company.setStateType(companyUpdatedDto.getStateType());
        company.setCategories(companyUpdatedDto.getCategories());
        company=companyRepository.save(company);
        Boolean newState=company.getStateType()==StateType.ACTIVE;
        if(oldState && newState){
        authenticationService.sendSetupEmail(companyUpdatedDto.getEmail(),companyUpdatedDto.getCompanyName());

        }
        return "Company inforormation has beed updated";
    }


    public CompanyType getCompanyType(String name) {
        Company company=companyRepository.findByName(name).orElseThrow();
        return company.getCompanyType();
    }
}
