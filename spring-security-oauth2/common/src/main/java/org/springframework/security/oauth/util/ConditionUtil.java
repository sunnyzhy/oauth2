package org.springframework.security.oauth.util;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.security.oauth.condition.TOKEN_STORE_STRATEGY;

public class ConditionUtil {
    private final static String property = "oauth.token.store.strategy";

    public static boolean matches(ConditionContext conditionContext, TOKEN_STORE_STRATEGY tokenStoreStrategy) {
        String strategy = conditionContext.getEnvironment().getProperty(property);
        return tokenStoreStrategy.getName().equalsIgnoreCase(strategy);
    }
}
