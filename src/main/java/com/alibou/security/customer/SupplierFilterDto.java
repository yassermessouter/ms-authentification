package com.alibou.security.customer;

import com.alibou.security.company.Category;
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
public class SupplierFilterDto {
    private Wilaya customerWilaya;
    private List<Category> categories;
}
