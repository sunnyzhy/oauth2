package org.springframework.security.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth.service.AuthenticationProviderImpl;
import org.springframework.security.oauth.service.UserDetailsServiceImpl;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@EnableWebSecurity
public class AuthenticationServerConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationProviderImpl authenticationProvider;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
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
}