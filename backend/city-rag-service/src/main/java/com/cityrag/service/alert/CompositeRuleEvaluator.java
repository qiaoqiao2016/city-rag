package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.service.alert.model.EvaluationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CompositeRuleEvaluator implements RuleEvaluator {

    @Autowired
    private List<RuleEvaluator> subEvaluators;

    @Override
    public Optional<AlertEventEntity> evaluate(EvaluationContext ctx) {
        String logic = ctx.getRule().getLogic();
        boolean isAnd = "AND".equalsIgnoreCase(logic);

        for (RuleEvaluator sub : subEvaluators) {
            if (sub instanceof CompositeRuleEvaluator) {
                continue;
            }
            Optional<AlertEventEntity> result = sub.evaluate(ctx);
            if (isAnd && result.isEmpty()) {
                return Optional.empty();
            }
            if (!isAnd && result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    @Override
    public String supportedType() {
        return "COMPOSITE";
    }
}
