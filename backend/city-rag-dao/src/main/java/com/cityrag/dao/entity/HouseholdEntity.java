package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("households")
public class HouseholdEntity extends BaseResourceEntity {

    private String householdCode;
    private String buildingId;
    private String floor;
    private String roomNumber;
    private String headName;
    private Integer memberCount;
    private Double area;
    private String ownership;
    private String district;
    private String street;
    private String community;
}
