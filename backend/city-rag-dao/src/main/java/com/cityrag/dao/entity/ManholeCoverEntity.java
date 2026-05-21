package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("manhole_covers")
public class ManholeCoverEntity extends BaseResourceEntity {

    private String coverCode;
    private String coverType;
    private String material;
    private String status;
    private Double waterLevel;
    private String geom;
}
