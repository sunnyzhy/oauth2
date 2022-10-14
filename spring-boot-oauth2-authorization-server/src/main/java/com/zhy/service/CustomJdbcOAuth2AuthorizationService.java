package com.zhy.service;

import com.zhy.constant.CommonConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhy
 * @date 2022/10/12 9:19
 */
public class CustomJdbcOAuth2AuthorizationService extends JdbcOAuth2AuthorizationService {
    private RedisTemplate<String, Object> redisTemplate;

    public CustomJdbcOAuth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository, RedisTemplate<String, Object> redisTemplate) {
        super(jdbcOperations, registeredClientRepository);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        super.save(authorization);

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            long expires = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()), accessToken, expires, TimeUnit.SECONDS);
        }
        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            long expires = ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()), refreshToken, expires, TimeUnit.SECONDS);
        }
        if (isOidcIdToken(authorization)) {
            OidcIdToken oidcIdToken = authorization.getToken(OidcIdToken.class).getToken();
            long expires = ChronoUnit.SECONDS.between(oidcIdToken.getIssuedAt(), oidcIdToken.getExpiresAt());
            redisTemplate.opsForValue().set(buildKey(OidcParameterNames.ID_TOKEN, oidcIdToken.getTokenValue()), oidcIdToken, expires, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        super.remove(authorization);

        List<String> keys = new ArrayList<>();
        if (isAccessToken(authorization)) {
            keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, authorization.getAccessToken().getToken().getTokenValue()));
        }
        if (isRefreshToken(authorization)) {
            keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, authorization.getRefreshToken().getToken().getTokenValue()));
        }
        redisTemplate.delete(keys);
    }

    private boolean isAccessToken(OAuth2Authorization authorization) {
        return authorization.getToken(OAuth2AccessToken.class) != null;
    }

    private boolean isRefreshToken(OAuth2Authorization authorization) {
        return authorization.getToken(OAuth2RefreshToken.class) != null;
    }

    private boolean isOidcIdToken(OAuth2Authorization authorization) {
        return authorization.getToken(OidcIdToken.class) != null;
    }

    private String buildKey(String type, String token) {
        return String.format(CommonConstant.TOKEN_REDIS_KEY_FORMAT, type, token);
    }
}
