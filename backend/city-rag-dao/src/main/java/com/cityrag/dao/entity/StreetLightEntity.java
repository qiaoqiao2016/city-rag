package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("street_lights")
public class StreetLightEntity extends BaseResourceEntity {

    private String lightCode;
    private String lightType;
    private Double poleHeight;
    private Double powerRating;
    private String status;
    private Integer brightness;
    private String controlMode;
    private String district;
    private String street;
    private String geom;
}
