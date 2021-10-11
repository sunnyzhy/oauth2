package org.springframework.security.oauth.config.authentication.server;

import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.condition.separation.BackEndCondition;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsUtils;

/**
 * @author zhouyi
 * @date 2021/1/12 15:07
 * 前后端不分离的认证服务配置
 */
@Order(value = 98)
@Conditional(BackEndCondition.class)
public class BackEndAuthenticationServerConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 允许 cors(跨站资源共享) 和 csrf(跨站请求伪造)
                .cors().and().csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/oauth2/keys").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
    }

}
