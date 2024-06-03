package com.alibou.security.customer;

import com.alibou.security.auth.AuthenticationRequest;
import com.alibou.security.auth.AuthenticationService;
import com.alibou.security.company.*;
import com.alibou.security.config.JwtService;
import com.alibou.security.user.StateType;
import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final FileRepository fileRepository;
    private final JwtService jwtService; ///hadi
    private final AuthenticationService authenticationService; //hadi

    public String register(CustomerRequestDto customerRequestDto) {
        List<Wilaya> wilayas=new ArrayList<>();
        wilayas.add(customerRequestDto.getWiliya());
        Company company= Company.builder()
                .name(customerRequestDto.getCompanyName())
                .email(customerRequestDto.getCompanyEmail())
                .stateType(StateType.INACTIVE)
                .companyType(CompanyType.CUSTOMER)
                .address(customerRequestDto.getAddress())
                .wilayas(wilayas)
                .number(customerRequestDto.getNumber())
                .build();
        Company company1=companyRepository.save(company);
        for (String fileUrl :customerRequestDto.getFileUrls()){
            FileMetadata fileMetadata=FileMetadata.builder()
                    .fileUrl(fileUrl)
                    .company(company1)
                    .build();
            fileRepository.save(fileMetadata);
        }
        User user= User.builder()
                .email(customerRequestDto.getEmail())
                .password(customerRequestDto.getPassword())
                .fullname(customerRequestDto.getFullName())
                .company(company1)
                .build();
        userRepository.save(user);
        return "customer regiser successfuly";
    }

    public CustomerResponseDto authenticate(AuthenticationRequest authenticationRequest) {
        User user=userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        Company company=user.getCompany();
        if (user.getCompany().getStateType()==StateType.ACTIVE){
            //verify the firebase token
            //if the token is true excute the code bellow
            var jwtToken = jwtService.generateToken(user);
            authenticationService.revokeAllUserTokens(user);
            authenticationService.saveUserToken(user, jwtToken);
            CustomerResponseDto customer= CustomerResponseDto.builder()
                    .id(company.getId())
                    .token(jwtToken)
                    .companyName(company.getName())
                    .fullName(user.getFullname())
                    .wiliya(company.getWilayas().get(0))
                    .address(company.getAddress())
                    .number(company.getNumber())
                    .email(user.getEmail())
                    .build();
            return customer;
        }else {
            throw new RuntimeException("Account inactive");
        }
    }

    public List<String> getSuppliers(Integer id) {
        Company customer=companyRepository.findById(id).orElseThrow();
        List<Company> companies=companyRepository.findByCompanyType(CompanyType.SUPPLIER);
        List<String> resultCompanies=new ArrayList<>();
        for (Company company:companies){
            if (company.getWilayas().contains(customer.getWilayas().get(0))
                    && isCategoryContains(company,customer.getCategories())){
                resultCompanies.add(company.getName());
            }
        }
        return resultCompanies;
    }
    public Boolean isCategoryContains(Company company,List<Category> categories){
        for (Category category:categories){
            if(company.getCategories().contains(category)){
                return true;
            }
        }
        return false;
    }


    public CompanyDto getSupplierProfile(Integer id) {
        Company company=companyRepository.findById(id).orElseThrow();
        CompanyDto companyDto= CompanyDto.builder()
                .description(company.getDescription())
                .address(company.getAddress())
                .number(company.getNumber())
                .email(company.getEmail())
                .name(company.getName())
                .build();
        return companyDto;
    }

    public String updateProfile(Integer id,ProfileRequestDto profileRequestDto) {
        List<Wilaya> wilayas=new ArrayList<>();
        wilayas.add(profileRequestDto.getWiliya());
        Company company=companyRepository.findById(id).orElseThrow();
        List<User> users=userRepository.findAllByCompany(company);
        User user=users.get(0);
        company.setAddress(profileRequestDto.getAddress());
        company.setNumber(profileRequestDto.getNumber());
        company.setWilayas(wilayas);
        companyRepository.save(company);
        user.setFullname(profileRequestDto.getFullName());
        userRepository.save(user);
        return "OK";

    }

}
