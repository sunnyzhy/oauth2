package org.springframework.security.oauth.config;

import org.springframework.security.oauth.condition.JwtCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@Conditional(JwtCondition.class)
public class JwtBeanConfig {
    @Value("${security.oauth2.resource.jwk.key-set-uri}")
    private String keySetUri;

    @Bean
    public TokenStore tokenStore() {
        return new JwkTokenStore(keySetUri);
    }
}
