package org.springframework.security.oauth.condition.separation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.oauth.util.ConditionUtil;

/**
 * 前后端不分离
 *
 * @author zhouyi
 * @date 2021/1/25 14:28
 */
public class BackEndCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return ConditionUtil.matchesSeparation(conditionContext, FRONT_BACK_SEPARATION.BACK_END);
    }
}
