package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fire_hydrants")
public class FireHydrantEntity extends BaseResourceEntity {

    private String hydrantCode;
    private String hydrantType;
    private Double pressure;
    private Double flowRate;
    private String status;
    private String district;
    private String street;
    private String geom;
}
