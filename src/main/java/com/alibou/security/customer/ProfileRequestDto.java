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
public class ProfileRequestDto {
    private String fullName;//
    private String number;
    private String name;
    private String email;
    private String imageUrl;
    private Wilaya wiliya;//
    private String address;//
}
