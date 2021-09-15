package org.springframework.security.oauth.config.authorization.server.jwt;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth.condition.JwtCondition;
import org.springframework.security.oauth.config.authorization.server.BaseAuthorizationServerConfig;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@EnableAuthorizationServer
@Conditional(JwtCondition.class)
public class JwtAuthorizationServerConfig extends BaseAuthorizationServerConfig {
    private final TokenStore tokenStore;

    public JwtAuthorizationServerConfig(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
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
        super.configure(endpoints);
        endpoints
                .tokenStore(tokenStore);
    }

}