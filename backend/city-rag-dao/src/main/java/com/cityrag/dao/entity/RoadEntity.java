package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("roads")
public class RoadEntity extends BaseResourceEntity {

    private String roadName;
    private String roadLevel;
    private Double length;
    private Double width;
    private Integer laneCount;
    private String maintenanceStatus;
    private String roadManager;
    private String geom;
}
