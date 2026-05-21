package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.dao.entity.AlertRuleEntity;
import com.cityrag.service.alert.model.EvaluationContext;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ThresholdRuleEvaluatorTest {

    private final ThresholdRuleEvaluator evaluator = new ThresholdRuleEvaluator();

    @Test
    void testSupportedType() {
        assertEquals("THRESHOLD", evaluator.supportedType());
    }

    @Test
    void testValueExceedsThreshold() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleId("rule-001");
        rule.setRuleName("水位超限");
        rule.setResourceType("manhole_cover");
        rule.setPriority("high");
        rule.setEvalInterval(60);
        rule.setConditions("[{\"metric\":\"water_level\",\"operator\":\">\",\"value\":0.8}]");

        EvaluationContext ctx = EvaluationContext.builder()
                .resourceId("MC-001")
                .resourceType("manhole_cover")
                .rule(rule)
                .telemetryData(Map.of("value", 1.2))
                .build();

        Optional<AlertEventEntity> result = evaluator.evaluate(ctx);
        assertTrue(result.isPresent());
        assertEquals("1.2", result.get().getTriggerValue());
    }

    @Test
    void testValueBelowThreshold() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setConditions("[{\"metric\":\"water_level\",\"value\":0.8}]");
        EvaluationContext ctx = EvaluationContext.builder()
                .resourceId("MC-001").rule(rule)
                .telemetryData(Map.of("value", 0.3)).build();
        assertTrue(evaluator.evaluate(ctx).isEmpty());
    }

    @Test
    void testNullTelemetryData() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setConditions("[{\"metric\":\"water_level\",\"value\":0.8}]");
        EvaluationContext ctx = EvaluationContext.builder()
                .resourceId("MC-001").rule(rule).build();
        assertTrue(evaluator.evaluate(ctx).isEmpty());
    }

    @Test
    void testNullThreshold() {
        EvaluationContext ctx = EvaluationContext.builder()
                .resourceId("MC-001").rule(new AlertRuleEntity())
                .telemetryData(Map.of("value", 1.0)).build();
        assertTrue(evaluator.evaluate(ctx).isEmpty());
    }
}
