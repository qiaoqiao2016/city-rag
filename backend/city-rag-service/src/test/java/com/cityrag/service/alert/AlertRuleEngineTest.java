package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.dao.entity.AlertRuleEntity;
import com.cityrag.dao.mapper.AlertEventMapper;
import com.cityrag.dao.mapper.AlertRuleMapper;
import com.cityrag.service.alert.model.EvaluationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertRuleEngineTest {

    @Mock private AlertRuleMapper alertRuleMapper;
    @Mock private AlertEventMapper alertEventMapper;
    @Mock private NotificationDispatcher notificationDispatcher;
    @Mock private ThresholdRuleEvaluator thresholdEvaluator;
    @Mock private RedissonClient redissonClient;
    @Captor private ArgumentCaptor<AlertEventEntity> eventCaptor;

    @InjectMocks
    private AlertRuleEngine alertRuleEngine;

    @Test
    void testFullRuleEvaluationFlow() {
        // Prepare: create a rule with threshold
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleId("rule-001");
        rule.setRuleName("水位超限");
        rule.setResourceType("manhole_cover");
        rule.setRuleType("THRESHOLD");
        rule.setConditions("[{\"metric\":\"water_level\",\"value\":0.8}]");
        rule.setPriority("high");
        rule.setEvalInterval(60);
        rule.setNotifyChannels("popup,sms");

        when(alertRuleMapper.selectByEnabledAndType(true, "manhole_cover"))
            .thenReturn(List.of(rule));

        when(thresholdEvaluator.supportedType()).thenReturn("THRESHOLD");

        AlertEventEntity expectedEvent = new AlertEventEntity();
        expectedEvent.setRuleId("rule-001");
        expectedEvent.setRuleName("水位超限");
        expectedEvent.setTriggerValue("1.2");
        when(thresholdEvaluator.evaluate(any(EvaluationContext.class)))
            .thenReturn(Optional.of(expectedEvent));

        // Execute
        alertRuleEngine.onTelemetryData("MC-001", "manhole_cover", Map.of("value", 1.2));

        // Verify: rule loaded
        verify(alertRuleMapper).selectByEnabledAndType(true, "manhole_cover");

        // Verify: event saved
        verify(alertEventMapper).insert(eventCaptor.capture());
        AlertEventEntity savedEvent = eventCaptor.getValue();
        assertEquals("rule-001", savedEvent.getRuleId());

        // Verify: notification dispatched
        verify(notificationDispatcher).dispatch(any(AlertEventEntity.class), eq(rule));
    }

    @Test
    void testNoActiveRules() {
        when(alertRuleMapper.selectByEnabledAndType(true, "camera"))
            .thenReturn(List.of());

        alertRuleEngine.onTelemetryData("CAM-001", "camera", Map.of("value", 99.0));

        verify(alertEventMapper, never()).insert(any());
        verify(notificationDispatcher, never()).dispatch(any(), any());
    }

    @Test
    void testDuplicatePrevention() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleId("rule-001");
        rule.setResourceType("manhole_cover");
        rule.setRuleType("THRESHOLD");
        rule.setConditions("[{\"metric\":\"water_level\",\"value\":0.8}]");

        when(alertRuleMapper.selectByEnabledAndType(true, "manhole_cover"))
            .thenReturn(List.of(rule));
        when(thresholdEvaluator.supportedType()).thenReturn("THRESHOLD");

        AlertEventEntity event = new AlertEventEntity();
        event.setRuleId("rule-001");
        when(thresholdEvaluator.evaluate(any(EvaluationContext.class)))
            .thenReturn(Optional.of(event));

        // First trigger
        alertRuleEngine.onTelemetryData("MC-001", "manhole_cover", Map.of("value", 1.2));
        verify(alertEventMapper, times(1)).insert(any());

        // Second trigger (same resource + rule, within dedup window)
        alertRuleEngine.onTelemetryData("MC-001", "manhole_cover", Map.of("value", 1.3));
        // Should NOT insert again (dedup)
        verify(alertEventMapper, times(1)).insert(any());
    }
}
