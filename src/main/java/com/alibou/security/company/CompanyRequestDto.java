package com.alibou.security.company;

import com.alibou.security.delivery.Region;
import com.alibou.security.delivery.Sector;
import com.alibou.security.delivery.Wilaya;
import com.alibou.security.user.StateType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDto {
    private String name;
    private String address;
    private List<Wilaya> wilayaList;
    private List<Region> regionList;
    private List<Sector> sectorList;


}
