package org.springframework.security.oauth.util;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.security.oauth.condition.cors.CORS_STRATEGY;
import org.springframework.security.oauth.condition.separation.FRONT_BACK_SEPARATION;
import org.springframework.security.oauth.condition.token.store.TOKEN_STORE_STRATEGY;

public class ConditionUtil {
    private final static String tokenStoreStrategyProperty = "oauth.token.store.strategy";
    private final static String frontAndBackSeparationProperty = "oauth.front-and-back-separation";
    private final static String corsProperty = "cors.strategy";

    public static boolean matchesTokenStore(ConditionContext conditionContext, TOKEN_STORE_STRATEGY tokenStoreStrategy) {
        String property = conditionContext.getEnvironment().getProperty(tokenStoreStrategyProperty);
        return tokenStoreStrategy.getName().equalsIgnoreCase(property);
    }

    public static boolean matchesSeparation(ConditionContext conditionContext, FRONT_BACK_SEPARATION frontBackSeparation) {
        String property = conditionContext.getEnvironment().getProperty(frontAndBackSeparationProperty);
        return frontBackSeparation.getName().equalsIgnoreCase(property);
    }

    public static boolean matchesCors(ConditionContext conditionContext, CORS_STRATEGY corsStrategy) {
        String property = conditionContext.getEnvironment().getProperty(corsProperty);
        return corsStrategy.getName().equalsIgnoreCase(property);
    }
}
