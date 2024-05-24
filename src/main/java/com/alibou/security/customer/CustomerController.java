package com.alibou.security.customer;

import com.alibou.security.auth.AuthenticationRequest;
import com.alibou.security.company.Company;
import com.alibou.security.company.Wilaya;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/client/regiter")
    public String register(@RequestBody CustomerRequestDto customerRequestDto){
        return customerService.register(customerRequestDto);
    }
    @PostMapping("/client/authenticate")
    public CustomerResponseDto authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return customerService.authenticate(authenticationRequest);
    }
    @GetMapping("/suppliers/{customer-id}")
    public List<String> getSuppliers(@PathVariable("customer-id") String id){
        return customerService.getSuppliers(Integer.parseInt(id));
    }
    @GetMapping("/supplier-profile/{supplier-id}")
    public CompanyDto getSupplierProfile(
            @PathVariable("supplier-id") String id){
        return customerService.getSupplierProfile(Integer.parseInt(id));
    }
    @PutMapping("/client/profile/{company-id}")
    public String updateProfile(
            @PathVariable("company-id") String name,
            @RequestBody ProfileRequestDto profileRequestDto){
        return customerService.updateProfile(Integer.parseInt(name),profileRequestDto);
    }


}
