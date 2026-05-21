package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("populations")
public class PopulationEntity extends BaseResourceEntity {

    private String idCard;
    private String name;
    private String gender;
    private String birthDate;
    private String phone;
    private String householdId;
    private String buildingId;
    private String residenceType;
    private String specialTag;
    private String district;
    private String street;
    private String community;
    private String gridId;
    private String geom;
}
