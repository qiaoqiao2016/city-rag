package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("buildings")
public class BuildingEntity extends BaseResourceEntity {

    private String buildingName;
    private String address;
    private String district;
    private String street;
    private String community;
    private Double totalArea;
    private Integer floorCount;
    private Double buildingHeight;
    private String structureType;
    private Integer buildYear;
    private String useType;
    private Double occupancyRate;
    private String geom;
}
