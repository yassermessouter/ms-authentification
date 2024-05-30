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


    @GetMapping("/companies/{company-id}")
    public CompanyDetailsDto findCompanyByName(
            @PathVariable("company-id") String id
    ){
        return companyService.findCompanyByname(Integer.parseInt(id));
    }
    @PutMapping("/companies")
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
