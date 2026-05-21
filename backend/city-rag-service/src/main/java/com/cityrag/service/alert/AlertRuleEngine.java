package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.dao.entity.AlertRuleEntity;
import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.dao.mapper.AlertEventMapper;
import com.cityrag.dao.mapper.AlertRuleMapper;
import com.cityrag.service.alert.model.EvaluationContext;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AlertRuleEngine {

    @Autowired
    private List<RuleEvaluator> evaluators;

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private AlertEventMapper alertEventMapper;

    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @Autowired(required = false)
    private RedissonClient redissonClient;

    private static final String DEDUP_KEY = "alert:dedup:";

    @PostConstruct
    public void init() {
        // Initialization if needed
    }

    public void onTelemetryData(String resourceId, String resourceType, Object telemetryData) {
        List<AlertRuleEntity> rules = alertRuleMapper.selectByEnabledAndType(true, resourceType);

        for (AlertRuleEntity rule : rules) {
            EvaluationContext ctx = EvaluationContext.builder()
                    .resourceId(resourceId)
                    .resourceType(resourceType)
                    .rule(rule)
                    .build();

            for (RuleEvaluator evaluator : evaluators) {
                if (evaluator.supportedType().equals(rule.getRuleType())) {
                    Optional<AlertEventEntity> eventOpt = evaluator.evaluate(ctx);
                    if (eventOpt.isPresent()) {
                        AlertEventEntity event = eventOpt.get();
                        if (!isDuplicate(event)) {
                            alertEventMapper.insert(event);
                            notificationDispatcher.dispatch(event, rule);
                        }
                    }
                    break;
                }
            }
        }
    }

    private boolean isDuplicate(AlertEventEntity event) {
        if (redissonClient == null) {
            return false;
        }
        String key = DEDUP_KEY + event.getResourceId() + ":" + event.getRuleId();
        RSet<String> dedupSet = redissonClient.getSet(key);
        return !dedupSet.add(event.getTriggerValue());
    }
}
