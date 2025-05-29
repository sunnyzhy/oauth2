package com.zhy.security;

import com.zhy.config.ApplicationConfig;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import javax.crypto.SecretKey;

/**
 * @author zhy
 * @date 2025/5/16 14:44
 */
@Configuration
public class TokenGeneratorConfig {
    @Resource
    private ApplicationConfig applicationConfig;

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(SecretKey secretKey, JwtEncoder jwtEncoder) {
        TokenGenerator.AccessTokenGenerator accessTokenGenerator = new TokenGenerator.AccessTokenGenerator(secretKey, applicationConfig.jwtExpiration);
        TokenGenerator.RefreshTokenGenerator refreshTokenGenerator = new TokenGenerator.RefreshTokenGenerator(secretKey, applicationConfig.jwtExpiration);
        JwtGenerator idTokenGenerator = new JwtGenerator(jwtEncoder);
        TokenGenerator.IdTokenCustomizer idTokenCustomizer = new TokenGenerator.IdTokenCustomizer();
        idTokenGenerator.setJwtCustomizer(idTokenCustomizer);
        // 必须配置accessToken、refreshToken和idToken，三者缺一不可
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, refreshTokenGenerator, idTokenGenerator);
    }
}
