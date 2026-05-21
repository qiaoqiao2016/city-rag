package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.service.alert.model.EvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ThresholdRuleEvaluator implements RuleEvaluator {

    @Override
    public Optional<AlertEventEntity> evaluate(EvaluationContext ctx) {
        // Extract metric value from telemetry data
        Object rawValue = ctx.getTelemetryData() != null ? ctx.getTelemetryData().get("value") : null;
        if (rawValue == null) {
            return Optional.empty();
        }
        try {
            double value = Double.parseDouble(rawValue.toString());
            Double threshold = ctx.getRule().getThreshold();
            if (threshold == null) {
                return Optional.empty();
            }
            if (value > threshold) {
                AlertEventEntity event = new AlertEventEntity();
                event.setRuleId(ctx.getRule().getRuleId());
                event.setRuleName(ctx.getRule().getRuleName());
                event.setResourceType(ctx.getResourceType());
                event.setResourceId(ctx.getResourceId());
                event.setTriggerValue(String.valueOf(value));
                event.setThreshold(String.valueOf(threshold));
                event.setPriority(ctx.getRule().getPriority());
                event.setDuration(ctx.getRule().getEvalInterval());
                return Optional.of(event);
            }
        } catch (NumberFormatException e) {
            // skip evaluation
        }
        return Optional.empty();
    }

    @Override
    public String supportedType() {
        return "THRESHOLD";
    }
}
