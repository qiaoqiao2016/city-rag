package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("alert_events")
public class AlertEventEntity {

    private String id;
    private String ruleId;
    private String ruleName;
    private String resourceType;
    private String resourceId;
    private String resourceName;
    private String priority;
    private String status;
    private String triggerValue;
    private String threshold;
    private Integer duration;
    private String confirmedBy;
    private LocalDateTime confirmedAt;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private String resolutionNote;
    private Integer triggerCount;
    private String geom;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
