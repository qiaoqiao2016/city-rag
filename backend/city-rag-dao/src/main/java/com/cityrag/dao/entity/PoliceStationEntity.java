package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("police_stations")
public class PoliceStationEntity extends BaseResourceEntity {

    private String stationName;
    private String stationType;
    private Integer personnelCount;
    private String dutyPhone;
    private String district;
    private String street;
    private String geom;
}
