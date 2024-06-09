package com.alibou.security.customer;

import com.alibou.security.auth.AuthenticationRequest;
import com.alibou.security.auth.AuthenticationService;
import com.alibou.security.auth.AuthenticationTokenRequest;
import com.alibou.security.company.*;
import com.alibou.security.config.JwtService;
import com.alibou.security.user.StateType;
import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final FileRepository fileRepository;
    private final JwtService jwtService; ///hadi
    private final AuthenticationService authenticationService; //hadi

    public Object register(CustomerRequestDto customerRequestDto) {
        try {
            List<Wilaya> wilayas = new ArrayList<>();
            wilayas.add(customerRequestDto.getWiliya());
            Company company = Company.builder()
                    .name(customerRequestDto.getCompanyName())
                    .email(customerRequestDto.getCompanyEmail())
                    .stateType(StateType.INACTIVE)
                    .companyType(CompanyType.CUSTOMER)
                    .address(customerRequestDto.getAddress())
                    .wilayas(wilayas)
                    .number(customerRequestDto.getNumber())
                    .build();
            Company company1 = companyRepository.save(company);
            for (String fileUrl : customerRequestDto.getFileUrls()) {
                FileMetadata fileMetadata = FileMetadata.builder()
                        .fileUrl(fileUrl)
                        .company(company1)
                        .build();
                fileRepository.save(fileMetadata);
            }
            User user = User.builder()
                    .email(customerRequestDto.getEmail())
                    .password(customerRequestDto.getPassword())
                    .fullname(customerRequestDto.getFullName())
                    .imageUrl(customerRequestDto.getImageUrl())
                    .company(company1)
                    .build();
            userRepository.save(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Customer registered successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());

        }
    }

    public Object authenticate(AuthenticationTokenRequest authenticationTokenRequest) {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        String token = authenticationTokenRequest.getToken();
        System.out.println(token);
        if (token == null) {
            ResponseEntity.status(404).body("Token is Missing");
        }

        try {
            //verify token
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
            String email = decodedToken.getEmail();
            System.out.println(email);
            //get data from db
            User user = userRepository.findByEmail(email).orElseThrow();
            Company company = user.getCompany();
            if (user.getCompany().getStateType() == StateType.ACTIVE) {
                var jwtToken = jwtService.generateToken(user);
                authenticationService.revokeAllUserTokens(user);
                authenticationService.saveUserToken(user, jwtToken);
                CustomerResponseDto customer = CustomerResponseDto.builder().id(company.getId()).imageUrl(user.getImageUrl()).token(jwtToken).companyName(company.getName()).fullName(user.getFullname()).wiliya(company.getWilayas().get(0)).address(company.getAddress()).number(company.getNumber()).email(user.getEmail()).favoritesProducts(company.getFavorites()).build();
                return customer;
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Account inactive");
                return ResponseEntity.status(401).body(errorResponse);
            }
        } catch (FirebaseAuthException e) {
            // Handle exception and return response with 498 status code
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid token: " + e.getMessage());
            return ResponseEntity.status(498).body(errorResponse);
        }

    }

    public Object getSuppliers(Integer id) {
        try {
            Company customer = companyRepository.findById(id).orElseThrow();
            List<Company> companies = companyRepository.findByCompanyType(CompanyType.SUPPLIER);
            List<SupplierResponseDto> resultCompanies = new ArrayList<>();
            for (Company company : companies) {

                resultCompanies.add(new SupplierResponseDto(company.getId(), company.getName(), ""));
//                if (company.getWilayas().contains(customer.getWilayas().get(0))
//                        && isCategoryContains(company, customer.getCategories())) {
//                    resultCompanies.add(company.getName());
//                }
            }

            return ResponseEntity.ok(resultCompanies);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    public Boolean isCategoryContains(Company company, List<Category> categories) {
        for (Category category : categories) {
            if (company.getCategories().contains(category)) {
                return true;
            }
        }
        return false;
    }


    public CompanyDto getSupplierProfile(Integer id) {
        Company company = companyRepository.findById(id).orElseThrow();
        CompanyDto companyDto = CompanyDto.builder()
                .id(company.getId())
                .description(company.getDescription())
                .address(company.getAddress())
                .number(company.getNumber())
                .email(company.getEmail())
                .name(company.getName())
                .wilaya(company.getWilayas().get(0))
                .build();
        return companyDto;
    }

    public Object updateProfile(Integer id, ProfileRequestDto profileRequestDto) {
        try {
            List<Wilaya> wilayas = new ArrayList<>();
            wilayas.add(profileRequestDto.getWiliya());
            Company company = companyRepository.findById(id).orElseThrow();
            List<User> users = userRepository.findAllByCompany(company);
            User user = users.get(0);
            company.setAddress(profileRequestDto.getAddress());

            company.setNumber(profileRequestDto.getNumber());
            company.setWilayas(wilayas);
            company.setName(profileRequestDto.getName());
            company.setEmail(profileRequestDto.getEmail());
            companyRepository.save(company);
            user.setFullname(profileRequestDto.getFullName());
            user.setImageUrl(profileRequestDto.getImageUrl());
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "profile saved successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }


    }

    public String getFavorites(Integer id) {
        Company company = companyRepository.findById(id).orElseThrow();
        return company.getFavorites();
    }

    public String addFavorites(Integer id, String favorites) {
        Company company = companyRepository.findById(id).orElseThrow();
        company.setFavorites(favorites);
        companyRepository.save(company);
        return "user favorites updated";
    }

}
