package com.cityrag.service.alert.model;

import com.cityrag.dao.entity.AlertRuleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationContext {

    private String resourceId;
    private String resourceType;
    private Map<String, Object> telemetryData;
    private AlertRuleEntity rule;
}
