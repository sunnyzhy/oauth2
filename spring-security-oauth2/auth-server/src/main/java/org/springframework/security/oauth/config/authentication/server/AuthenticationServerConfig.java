package org.springframework.security.oauth.config.authentication.server;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth.config.OAuthConfig;
import org.springframework.security.oauth.service.*;
import org.springframework.web.cors.CorsUtils;

/**
 * @author zhouyi
 * @date 2021/1/12 15:07
 * 前后端分离的认证服务配置
 */
@EnableWebSecurity
public class AuthenticationServerConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationProviderImpl authenticationProvider;
    private final OAuthConfig oAuthConfig;

    public AuthenticationServerConfig(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService, AuthenticationProviderImpl authenticationProvider, OAuthConfig oAuthConfig) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.oAuthConfig = oAuthConfig;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/resources/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 注册自定义的userDetailsService
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        // 注册自定义认证
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 允许 cors(跨站资源共享) 和 csrf(跨站请求伪造)
                .cors().and().csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(oAuthConfig.getPermit()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(oAuthConfig.getLoginPage())
                .loginProcessingUrl(oAuthConfig.getLoginProcessingUrl())
                .permitAll();
    }

}