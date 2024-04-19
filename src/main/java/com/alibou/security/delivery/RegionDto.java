package com.alibou.security.delivery;

import com.alibou.security.company.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionDto {
    private String name;
    private String date;

    public Region regionMapper(Company company){
        return Region.builder()
                .name(this.name)
                .date(this.date)
                .company(company)
                .build();
    }
}
