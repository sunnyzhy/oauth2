package org.springframework.security.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthBean {
    @Bean
    @ConfigurationProperties(prefix = "oauth.http")
    public OAuthConfig oAuthConfig() {
        return new OAuthConfig();
    }
}
