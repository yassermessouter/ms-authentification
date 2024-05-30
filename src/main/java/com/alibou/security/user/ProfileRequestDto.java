package com.alibou.security.user;

import com.alibou.security.company.Wilaya;
import com.alibou.security.role.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDto {
    private String fullname;
    private String companyName;
    private String adress;
    private String number;
    private String description;
    private Boolean hasDeliveryDates;
    private String companyEmail;
    private List<Wilaya> wilayas;
}
