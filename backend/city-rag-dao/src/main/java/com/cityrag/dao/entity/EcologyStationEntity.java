package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecology_stations")
public class EcologyStationEntity extends BaseResourceEntity {

    private String stationName;
    private String stationType;
    private String monitorFactors;
    private String status;
    private String district;
    private String street;
    private String geom;
}
