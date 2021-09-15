package org.springframework.security.oauth.config.token.store.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.oauth.condition.RedisCondition;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@Conditional(RedisCondition.class)
public class RedisBean {
    private final LettuceConnectionFactory lettuceConnectionFactory;

    public RedisBean(LettuceConnectionFactory lettuceConnectionFactory) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(lettuceConnectionFactory);
    }

}
