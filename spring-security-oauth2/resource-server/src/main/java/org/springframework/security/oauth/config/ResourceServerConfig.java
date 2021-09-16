package org.springframework.security.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth.service.AuthenticationEntryPointImpl;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final TokenStore tokenStore;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    public ResourceServerConfig(TokenStore tokenStore, AuthenticationEntryPointImpl authenticationEntryPoint) {
        this.tokenStore = tokenStore;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore)
                // 自定义 AuthenticationEntryPoint，实现如果没有授权就直接跳转到认证页面。
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 请求 /product/** 不需要认证授权
                .antMatchers("/product/**").permitAll()
                // 其它的请求需要认证授权
                .anyRequest().authenticated();
    }

}
