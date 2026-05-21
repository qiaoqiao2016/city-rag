package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.service.alert.model.EvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BaselineRuleEvaluator implements RuleEvaluator {

    @Override
    public Optional<AlertEventEntity> evaluate(EvaluationContext ctx) {
        Object currentValue = ctx.getTelemetryData().get("value");
        Object historicalAvg = ctx.getTelemetryData().get("historicalAvg");
        if (currentValue == null || historicalAvg == null) {
            return Optional.empty();
        }
        try {
            double current = Double.parseDouble(currentValue.toString());
            double baseline = Double.parseDouble(historicalAvg.toString());
            double deviation = Math.abs(current - baseline) / baseline;
            double threshold = Double.parseDouble(ctx.getRule().getThreshold());
            if (deviation > threshold) {
                AlertEventEntity event = new AlertEventEntity();
                event.setRuleId(ctx.getRule().getRuleId());
                event.setRuleName(ctx.getRule().getRuleName());
                event.setResourceType(ctx.getResourceType());
                event.setResourceId(ctx.getResourceId());
                event.setTriggerValue(String.valueOf(current));
                event.setThreshold(String.valueOf(deviation));
                event.setPriority(ctx.getRule().getPriority());
                return Optional.of(event);
            }
        } catch (NumberFormatException e) {
            // skip
        }
        return Optional.empty();
    }

    @Override
    public String supportedType() {
        return "BASELINE";
    }
}
