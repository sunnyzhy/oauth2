package com.zhy.controller;

import com.zhy.config.ApplicationConfig;
import com.zhy.util.StringUtil;
import com.zhy.vo.RegisteredClientVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * @author zhy
 * @date 2025/5/29 10:45
 */
@RestController
@RequestMapping("/api/client")
public class ApiController {
    @Resource
    private RegisteredClientRepository registeredClientRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ApplicationConfig applicationConfig;

    @PostMapping
    public String save(@RequestBody RegisteredClientVo entity) {
        RegisteredClient client = registeredClientRepository.findByClientId(entity.getClientId());
        String id = client == null ? StringUtil.uuidTrim() : client.getId();
        if (StringUtils.isEmpty(entity.getClientName())) {
            if (client == null) {
                entity.setClientName(id);
            } else {
                entity.setClientName(client.getClientName());
            }
        }
        RegisteredClient registeredClient = RegisteredClient.withId(id)
                .clientId(entity.getClientId())
                .clientSecret(passwordEncoder.encode(entity.getClientSecret()))
                .clientName(entity.getClientName())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri(entity.getRedirectUri())
                .postLogoutRedirectUri("/oauth/logout")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.ofSeconds(applicationConfig.jwtExpiration))
                        .build())
                .build();
        registeredClientRepository.save(registeredClient);
        return "ok";
    }

}
