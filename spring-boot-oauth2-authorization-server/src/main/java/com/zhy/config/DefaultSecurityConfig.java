package com.zhy.config;

import com.zhy.constant.CommonConstant;
import com.zhy.service.CustomDaoAuthenticationProvider;
import com.zhy.service.CustomLogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author zhy
 * @date 2022/10/12 8:59
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class DefaultSecurityConfig {
    private final CustomDaoAuthenticationProvider customDaoAuthenticationProvider;

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/oauth/**", "/client/**", "/css/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 自定义 DaoAuthenticationProvider
//                .authenticationProvider(customDaoAuthenticationProvider)
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage(CommonConstant.loginUrl)
                        .loginProcessingUrl(CommonConstant.loginProcessingUrl))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl(CommonConstant.logoutUrl)
                        .addLogoutHandler(new CustomLogoutHandler())
                );
        return http.build();
    }

}
