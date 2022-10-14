package com.zhy.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.zhy.entity.CustomUser;
import com.zhy.util.JwkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author zhy
 * @date 2022/10/12 9:55
 */
@Configuration
public class JwtConfig {
    @Value("${oauth2.jwt-secret:sunny_zhy}")
    private String jwtSecret;

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        JWK jwk = JwkUtil.hmac(jwtSecret, JwkUtil.HMAC.HS512);
        JWKSet jwkSet = new JWKSet(jwk);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            CustomUser user = (CustomUser) context.getPrincipal().getPrincipal();
            JwtClaimsSet.Builder claims = context.getClaims();

            // 指定 header 的加密算法，与 JWKSource 里的加密算法一致
//            context.getHeaders().algorithm(SignatureAlgorithm.RS512);
//            context.getHeaders().algorithm(SignatureAlgorithm.ES512);
            context.getHeaders().algorithm(MacAlgorithm.HS512);

            if (context.getTokenType().getValue().equals(OAuth2TokenType.ACCESS_TOKEN.getValue())) {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                claims.claim("sub", user.getUsername());
                claims.claim("id", id);
                claims.claim("userId", user.getUserId());
                claims.claim("email", user.getEmail());
                claims.claim("phone", user.getPhone());
            }
            if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                // TODO
                // 添加用户的其它信息

                claims.claim("sub", user.getUsername());
                claims.claim("userId", user.getUserId());
                claims.claim("email", user.getEmail());
                claims.claim("phone", user.getPhone());
            }
        };
    }
}
