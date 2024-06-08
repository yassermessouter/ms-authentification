package com.alibou.security.customer;

import com.alibou.security.company.Wilaya;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {
    private String email;
    private String password;
    private String fullName;
    private String number;
    private String companyName;
    private List<String> fileUrls;
    private Wilaya wiliya;
    private String address;
    private String companyEmail;
    private String imageUrl;
}
