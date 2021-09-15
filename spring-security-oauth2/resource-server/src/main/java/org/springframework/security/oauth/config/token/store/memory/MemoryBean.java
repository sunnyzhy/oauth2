package org.springframework.security.oauth.config.token.store.memory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth.condition.MemoryCondition;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@Conditional(MemoryCondition.class)
public class MemoryBean {
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

}
