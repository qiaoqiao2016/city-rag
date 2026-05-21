package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.service.alert.model.EvaluationContext;

import java.util.Optional;

public interface RuleEvaluator {

    Optional<AlertEventEntity> evaluate(EvaluationContext ctx);

    String supportedType();
}
