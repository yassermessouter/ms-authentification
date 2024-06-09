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
public class CompanyDto {
    private String name;
    private String email;
    private String address;
    private String number;
    private Wilaya wilaya;
    private Integer id;
    private String description;
}
