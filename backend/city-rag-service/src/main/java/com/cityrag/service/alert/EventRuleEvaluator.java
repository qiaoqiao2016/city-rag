package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.service.alert.model.EvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventRuleEvaluator implements RuleEvaluator {

    @Override
    public Optional<AlertEventEntity> evaluate(EvaluationContext ctx) {
        Object eventCode = ctx.getTelemetryData().get("eventCode");
        if (eventCode == null) {
            return Optional.empty();
        }
        String code = eventCode.toString();
        // Match event code against condition patterns
        if (matchesCondition(code, ctx.getRule().getConditions())) {
            AlertEventEntity event = new AlertEventEntity();
            event.setRuleId(ctx.getRule().getRuleId());
            event.setRuleName(ctx.getRule().getRuleName());
            event.setResourceType(ctx.getResourceType());
            event.setResourceId(ctx.getResourceId());
            event.setTriggerValue(code);
            event.setPriority(ctx.getRule().getPriority());
            return Optional.of(event);
        }
        return Optional.empty();
    }

    private boolean matchesCondition(String eventCode, String conditions) {
        if (conditions == null || conditions.isBlank()) {
            return true;
        }
        // Simple comma-separated event code matching
        String[] codes = conditions.split(",");
        for (String c : codes) {
            if (c.trim().equals(eventCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String supportedType() {
        return "EVENT";
    }
}
