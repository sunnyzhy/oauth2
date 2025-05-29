package com.zhy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhy
 * @date 2025/5/28 18:16
 */
@Configuration
public class ApplicationConfig {
    @Value("${oauth2.jwt-secret}")
    public String jwtSecret;
    @Value("${oauth2.jwt-expiration}")
    public Long jwtExpiration;
}
