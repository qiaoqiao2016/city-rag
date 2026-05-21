package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprises")
public class EnterpriseEntity extends BaseResourceEntity {

    private String enterpriseName;
    private String creditCode;
    private String legalPerson;
    private String phone;
    private String industryType;
    private String enterpriseScale;
    private Integer employeeCount;
    private Double annualRevenue;
    private String buildingId;
    private String district;
    private String street;
    private String community;
    private String geom;
}
