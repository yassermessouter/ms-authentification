package com.alibou.security.user;

import com.alibou.security.company.*;
import com.alibou.security.role.Permission;
import com.alibou.security.role.Role;
import com.alibou.security.token.Token;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Integer userId;
    private String name;
    private String email;
    private List<Permission> permissions;
    //
    private Integer companyId;
    private String companyName;
    private String address;
    private Boolean hasDeliveryDate;
    private String number;
    private String description;
    private String companyEmail;
    private List<Category> categories;
    private List<Wilaya> wilayas;




}
