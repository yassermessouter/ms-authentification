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
public class CustomerResponseDto {
    private Integer id;
    private String token;
    private String companyName;
    private String fullName;
    private String email;
    private String number;
    private Wilaya wiliya;
    private String address;
    private String imageUrl;
}
