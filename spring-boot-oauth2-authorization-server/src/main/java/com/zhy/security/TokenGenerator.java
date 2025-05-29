package com.zhy.security;

import com.zhy.util.StringUtil;
import com.zhy.entity.CustomUser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhy
 * @date 2025/5/16 14:37
 */
public class TokenGenerator {
    /**
     * 默认的codeGenerator生成的授权码长度是128，此处把长度调整为8
     */
    static class AuthorizationCodeGenerator implements OAuth2TokenGenerator<OAuth2AuthorizationCode> {
        private final SecureRandom secureRandom = new SecureRandom();

        @Override
        public OAuth2AuthorizationCode generate(OAuth2TokenContext context) {
            if (context.getTokenType() == null || !"code".equals(context.getTokenType().getValue())) {
                return null;
            }
            byte[] randomBytes = new byte[6];
            secureRandom.nextBytes(randomBytes);
            String encode = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getAuthorizationCodeTimeToLive());
            return new OAuth2AuthorizationCode(encode, issuedAt, expiresAt);
        }
    }

    static class AccessTokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {
        private SecretKey secretKey;
        private Long expiration;

        AccessTokenGenerator(SecretKey secretKey, Long expiration) {
            this.secretKey = secretKey;
            this.expiration = expiration;
        }

        @Override
        public OAuth2AccessToken generate(OAuth2TokenContext context) {
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) || !OAuth2TokenFormat.REFERENCE.equals(context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
                return null;
            }
            TokenInfo tokenInfo = getTokenInfo(context, secretKey, expiration);
            return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                    tokenInfo.token, tokenInfo.issuedAt, tokenInfo.expiresAt, context.getAuthorizedScopes());
        }
    }

    static class RefreshTokenGenerator implements OAuth2TokenGenerator<OAuth2RefreshToken> {
        private SecretKey secretKey;
        private Long expiration;

        RefreshTokenGenerator(SecretKey secretKey, Long expiration) {
            this.secretKey = secretKey;
            this.expiration = expiration;
        }

        @Override
        public OAuth2RefreshToken generate(OAuth2TokenContext context) {
            if (!OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
                return null;
            }
            TokenInfo tokenInfo = getTokenInfo(context, secretKey, expiration);
            return new OAuth2RefreshToken(tokenInfo.token, tokenInfo.issuedAt, tokenInfo.expiresAt);
        }
    }

    static class IdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
        @Override
        public void customize(JwtEncodingContext context) {
            if (!context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                return;
            }
            // 添加必要的OIDC声明
            context.getClaims().claims(claims -> {
                Object principal = context.getPrincipal().getPrincipal();
                if (!(principal instanceof CustomUser)) {
                    return;
                }
                CustomUser user = (CustomUser) principal;
                claims.put("name", user.getUsername());
                claims.put("email", user.getEmail());
            });
        }
    }

    private static TokenInfo getTokenInfo(OAuth2TokenContext context, SecretKey secretKey, Long expiration) {
        Object principal = context.getPrincipal().getPrincipal();
        if (!(principal instanceof CustomUser)) {
            return null;
        }
        CustomUser user = (CustomUser) principal;
        String id = StringUtil.uuidTrim();
        String sessionID = StringUtil.uuidTrim();
        final Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUsername());
        claims.put("tenant_id", user.getTenantId());
        claims.put("id", id);
        claims.put("created", new Date());
        claims.put("session_id", sessionID);
        String token = Jwts.builder()
                .claims(claims)
                .expiration(generateExpirationDate(expiration))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getAccessTokenTimeToLive());
        return new TokenInfo()
                .token(token)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt);
    }

    private static Date generateExpirationDate(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    static class TokenInfo {
        private String token;
        private Instant issuedAt;
        private Instant expiresAt;

        private TokenInfo() {

        }

        public TokenInfo token(String token) {
            this.token = token;
            return this;
        }

        public TokenInfo issuedAt(Instant issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public TokenInfo expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
    }

}
