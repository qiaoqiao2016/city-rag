package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cameras")
public class CameraEntity extends BaseResourceEntity {

    private String cameraName;
    private String cameraType;
    private String resolution;
    private Double installHeight;
    private String direction;
    private String ipAddress;
    private String streamUrl;
    private String protocol;
    private Boolean onlineStatus;
    private String geom;
    private String aiCapability;
}
