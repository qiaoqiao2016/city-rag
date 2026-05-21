package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("grids")
public class GridEntity extends BaseResourceEntity {

    private String gridCode;
    private String gridType;
    private String gridMaster;
    private String gridWorker;
    private Integer populationCount;
    private Integer buildingCount;
    private Integer enterpriseCount;
    private String geom;
}
