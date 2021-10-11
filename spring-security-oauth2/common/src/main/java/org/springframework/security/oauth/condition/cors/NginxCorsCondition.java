package org.springframework.security.oauth.condition.cors;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.oauth.condition.token.store.TOKEN_STORE_STRATEGY;
import org.springframework.security.oauth.util.ConditionUtil;

public class NginxCorsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return ConditionUtil.matchesCors(conditionContext, CORS_STRATEGY.NGINX);
    }
}
