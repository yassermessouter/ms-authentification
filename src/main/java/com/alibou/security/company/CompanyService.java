package com.alibou.security.company;

import com.alibou.security.delivery.*;
import com.alibou.security.role.Role;
import com.alibou.security.token.Token;
import com.alibou.security.token.TokenRepository;
import com.alibou.security.user.User;
import com.alibou.security.user.UserInfosDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final WilayaRepository wilayaRepository;
    private final RegionRepository regionRepository;
    private final SectorRepository sectorRepository;
    private final TokenRepository tokenRepository;

    public List<CompanyResponseDto> findAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> new CompanyResponseDto(company.getName(), company.getAddress(), company.getStateType()))
                .collect(Collectors.toList());
    }

    public CompanyDetailsDto findCompanyByname(String name) {
        Company company= companyRepository.findByName(name).orElseThrow();
        return CompanyDetailsDto.builder()
                .name(company.getName())
                .address(company.getAddress())
                .tradeRegistry(company.getTradeRegistry())
                .stateType(company.getStateType())
                .categories(company.getCategories())
                .build();
    }

    public String updateCompanyDetails(CompanyUpdatedDto companyUpdatedDto) {
        Company company=companyRepository.findByName(companyUpdatedDto.getName()).orElseThrow();
        company.setStateType(companyUpdatedDto.getStateType());
        company.setCategories(companyUpdatedDto.getCategories());
        company=companyRepository.save(company);
        return "Company inforormation has beed updated";
    }

    public Company setupCompanyDetails(CompanySetupDto companySetupDto) {
        Company company=companyRepository.findByName(companySetupDto.getName()).orElseThrow();
        for(WilayaDto wilayaDto:companySetupDto.getWilayaList()){
            wilayaRepository.save(wilayaDto.wilayaMapper(company));
        }
        for(RegionDto regionDto:companySetupDto.getRegionList()){
            regionRepository.save(regionDto.regionMapper(company));
        }
        for(SectorDto sectorDto:companySetupDto.getSectorList()){
            sectorRepository.save(sectorDto.sectorMapper(company));
        }
        return  company;
    }

    public CompanyRequestDto getCompanyInfos(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            Token jwt = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
            if (!jwt.isExpired() && !jwt.isRevoked()) {
                User user = jwt.getUser();
                Company company=user.getCompany();
                return CompanyRequestDto.builder()
                        .name(company.getName())
                        .address(company.getAddress())
                        .wilayaList(company.getWilayaList())
                        .regionList(company.getRegionList())
                        .sectorList(company.getSectorList())
                        .build();
            } else {
                throw new RuntimeException("Token invalide");
            }
        } else {
            throw new RuntimeException("Authorization header missing or invalid");
        }
    }
}
