package org.springframework.security.oauth.config.authentication.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.condition.separation.FrontEndCondition;
import org.springframework.security.oauth.service.*;
import org.springframework.web.cors.CorsUtils;

/**
 * @author zhouyi
 * @date 2021/1/12 15:07
 * 前后端分离的认证服务配置
 */
@Order(value = 99)
@Configuration
@Conditional(FrontEndCondition.class)
public class FrontEndAuthenticationServerConfig extends WebSecurityConfigurerAdapter {
    @Value("${login.page-url}")
    private String loginPage;
    private final ForwardAuthenticationSuccessHandler forwardAuthenticationSuccessHandler;
    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    private final AuthenticationFailureHandlerImpl authenticationFailureHandler;

    public FrontEndAuthenticationServerConfig(ForwardAuthenticationSuccessHandler forwardAuthenticationSuccessHandler, AuthenticationSuccessHandlerImpl authenticationSuccessHandler, AuthenticationFailureHandlerImpl authenticationFailureHandler) {
        this.forwardAuthenticationSuccessHandler = forwardAuthenticationSuccessHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 允许 cors(跨站资源共享) 和 csrf(跨站请求伪造)
                .cors().and().csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/oauth2/keys", "/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // 自定义登录页 url,默认为 SpringSecurity 的 /login
                .loginPage(loginPage)
//                .usernameParameter("username").passwordParameter("password")
                // 自定义后台处理登录请求的接口，默认是 loginPage 的 url
                // 当设置了 loginProcessingUrl，例如 loginProcessingUrl("/auth/login")，并不是真的需要一个 /auth/login 接口，而只是需要你将自定义的前端登录页请求的后台 url 改成 /auth/login 即可
                // 过滤器 UsernamePasswordAuthenticationFilter 仅由 loginProcessingUrl 中指定的 url 调用，并将使用 username-parameter 和 password-parameter 的值作为请求中的用户名和密码
                .loginProcessingUrl("/login")
//        .successForwardUrl("http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://20.0.0.106")
                // 认证成功处理器
//                .successHandler(authenticationSuccessHandler)
//                .successHandler(forwardAuthenticationSuccessHandler)
                // 认证失败处理器
                .failureHandler(authenticationFailureHandler).permitAll();
    }

}
