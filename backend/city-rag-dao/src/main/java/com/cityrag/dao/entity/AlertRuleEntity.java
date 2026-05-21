package com.cityrag.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@TableName("alert_rules")
public class AlertRuleEntity {

    private String ruleId;
    private String ruleName;
    private String resourceType;
    private String ruleType;
    private Boolean enabled;
    private String priority;
    private String conditions;
    private String logic;
    private Integer evalInterval;
    private String notifyChannels;
    private String notifyRoles;
    private Integer escalationDelay;
    private Boolean autoRecover;
    private String mutePeriods;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Parse conditions JSON and extract threshold value from the first condition.
     */
    @SneakyThrows
    public Double getThreshold() {
        if (this.conditions == null || this.conditions.isBlank()) {
            return null;
        }
        List<Map<String, Object>> conds = MAPPER.readValue(this.conditions,
                new TypeReference<List<Map<String, Object>>>() {});
        if (!conds.isEmpty() && conds.get(0).containsKey("value")) {
            Object val = conds.get(0).get("value");
            return val instanceof Number ? ((Number) val).doubleValue() : Double.parseDouble(val.toString());
        }
        return null;
    }

    /**
     * Parse conditions JSON into a list of condition maps.
     */
    @SneakyThrows
    public List<Map<String, Object>> getConditionsList() {
        if (this.conditions == null || this.conditions.isBlank()) {
            return List.of();
        }
        return MAPPER.readValue(this.conditions,
                new TypeReference<List<Map<String, Object>>>() {});
    }

