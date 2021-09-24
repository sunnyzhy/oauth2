package org.springframework.security.oauth.config.authorization.server.memory;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth.condition.token.store.MemoryCondition;
import org.springframework.security.oauth.service.UserDetailsServiceImpl;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@EnableAuthorizationServer
@Conditional(MemoryCondition.class)
public class MemoryAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final TokenStore tokenStore;
    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenConverter accessTokenConverter;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public MemoryAuthorizationServerConfig(TokenStore tokenStore, AuthenticationManager authenticationManager, JwtAccessTokenConverter accessTokenConverter, UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.accessTokenConverter = accessTokenConverter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String secret = passwordEncoder.encode("secret");
        clients
                .inMemory()
                .withClient("messaging-client")
                .secret(secret)
                .redirectUris("http://localhost")
                .authorizedGrantTypes("refresh_token", "authorization_code")
                .accessTokenValiditySeconds(25200)
                .refreshTokenValiditySeconds(25200)
                .scopes("READ,WRITE")
                .autoApprove(true);
    }

    /**
     * 注意:
     * 1. tokenServices 与 accessTokenConverter 不能同时配置，否则系统只会加载 tokenServices 作为默认的配置, 其生成的 token 是调用 KeyGenerators.secureRandom(20) 生成的字符串
     * 2. AccessTokenConverter 可以在 AuthorizationServerTokenServices 里定义
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

}
