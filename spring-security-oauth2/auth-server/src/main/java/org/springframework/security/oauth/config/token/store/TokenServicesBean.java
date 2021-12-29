package org.springframework.security.oauth.config.token.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author zhy
 * @date 2021/11/3 17:51
 */
@Configuration
public class TokenServicesBean {
    private final JdbcClientDetailsService jdbcClientDetailsService;
    private final TokenStore tokenStore;
    private final JwtAccessTokenConverter accessTokenConverter;

    public TokenServicesBean(JdbcClientDetailsService jdbcClientDetailsService, TokenStore tokenStore, JwtAccessTokenConverter accessTokenConverter) {
        this.jdbcClientDetailsService = jdbcClientDetailsService;
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
    }

    /**
     * tokenServices 需要与 tokenStore、tokenEnhancer 关联，这三者的存储模式(jdbc、jwt)必须一致
     *
     * 疑问: tokenServices Bean 被 Autowired 的时候，取出来的对象所绑定的属性既不是自定义的属性，也不是默认的属性
     * 所以，暂时注释掉此处的 tokenServices Bean 定义
     *
     * @return
     */
//    @Primary
//    @Bean
//    public AuthorizationServerTokenServices tokenServices() {
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setClientDetailsService(jdbcClientDetailsService);//客户端详情服务
//        tokenServices.setSupportRefreshToken(true);//支持刷新令牌
//        //service.setReuseRefreshToken(true); // 复用refresh token
//        tokenServices.setTokenStore(tokenStore);//令牌存储策略
//        //令牌增强
////        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
////        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer));
////        tokenServices.setTokenEnhancer(tokenEnhancerChain);
//        tokenServices.setTokenEnhancer(accessTokenConverter);
//        return tokenServices;
//    }
}
