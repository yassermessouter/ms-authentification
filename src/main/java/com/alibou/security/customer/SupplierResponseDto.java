package com.alibou.security.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SupplierResponseDto {
    private Integer id;
    private String name;
    private String imageUrl;

}
