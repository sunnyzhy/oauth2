package com.zhy.controller;

import com.zhy.entity.RegisteredClientVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

/**
 * @author zhy
 * @date 2022/10/12 9:16
 */
@RestController
@RequestMapping(value = "/client")
@RequiredArgsConstructor
public class OAuth2ClientController {
    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public String save(@RequestBody RegisteredClientVo clientVo) {
        Assert.notNull(clientVo, "client 不能为空");
        Assert.hasLength(clientVo.getClientId(), "参数错误");
        Assert.hasLength(clientVo.getClientName(), "参数错误");
        Assert.hasLength(clientVo.getClientSecret(), "参数错误");
        Assert.hasLength(clientVo.getRedirectUri(), "参数错误");
        if (null != registeredClientRepository.findByClientId(clientVo.getClientId())) {
            return "client 已存在";
        }
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientVo.getClientId())
                .clientSecret(passwordEncoder.encode(clientVo.getClientSecret()))
                .clientName(clientVo.getClientName())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(clientVo.getRedirectUri())
                .scopes(s -> {
                    s.add(OidcScopes.OPENID);
                    s.add(OidcScopes.PROFILE);
                    s.add(OidcScopes.EMAIL);
                    s.add(OidcScopes.ADDRESS);
                    s.add(OidcScopes.PHONE);
                    s.addAll(clientVo.getScopes());
                })
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false) // 无需确认授权
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .refreshTokenTimeToLive(Duration.ofDays(3))
                        .reuseRefreshTokens(true)
                        .build())
                .build();
        registeredClientRepository.save(registeredClient);

        return "添加成功";
    }
}
