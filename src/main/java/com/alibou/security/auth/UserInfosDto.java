package com.alibou.security.auth;

import com.alibou.security.role.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfosDto {
    private String email;
    private String fullName;
    private String companyName;
    private List<Permission> permissions;
}
