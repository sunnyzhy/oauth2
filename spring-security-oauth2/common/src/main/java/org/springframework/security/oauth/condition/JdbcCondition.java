package org.springframework.security.oauth.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.oauth.util.ConditionUtil;

/**
 * token 的存储策略是 jdbc
 *
 * @author zhouyi
 * @date 2021/1/25 14:28
 */
public class JdbcCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return ConditionUtil.matches(conditionContext, TOKEN_STORE_STRATEGY.JDBC);
    }
}
