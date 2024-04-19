package com.alibou.security.company;

import com.alibou.security.delivery.Wilaya;
import com.alibou.security.delivery.WilayaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companies")
    public List<CompanyResponseDto> findCompanies() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/companies/{company-name}")
    public CompanyDetailsDto findCompanyByName(
            @PathVariable("company-name") String name
    ){
        return companyService.findCompanyByname(name);
    }

    @PutMapping("/companies/{company-name}")
    public String updateCompanyDetails(
            @RequestBody CompanyUpdatedDto companyUpdatedDto
    ){
        return companyService.updateCompanyDetails(companyUpdatedDto);
    }


    @GetMapping("/company-infos")
    public CompanyRequestDto getCompanyInfos(
            HttpServletRequest request
    ){
        return companyService.getCompanyInfos(request);
    }

    @PutMapping("/update-company-infos")
    public Company updateCompanyInfos(
            HttpServletRequest request,
            @RequestBody CompanyRequestDto companyRequest
    ){
        return companyService.update(request,companyRequest);
    }





}
