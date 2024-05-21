package com.alibou.security.company;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from http://localhost:3000
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
    @PutMapping("/companie")
    public String updateCompanyDetails(
            @RequestBody CompanyUpdatedDto companyUpdatedDto
    ){
        return companyService.updateCompanyDetails(companyUpdatedDto);
    }
    @GetMapping("/company-type")
    public CompanyType getCompanyType(@RequestBody String name){
        return companyService.getCompanyType(name);
    }






}
