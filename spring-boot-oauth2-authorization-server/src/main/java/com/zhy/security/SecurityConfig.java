package com.zhy.security;

import com.zhy.constant.CommonConstant;
import com.zhy.services.CustomUserDetailsService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.Map;

/**
 * @author zhy
 * @date 2025/5/16 14:35
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Resource
    private CustomUserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RegisteredClientRepository registeredClientRepository;
    @Resource
    private OAuth2AuthorizationService authorizationService;
    @Resource
    private OAuth2AuthorizationConsentService authorizationConsentService;
    @Resource
    private CustomLogoutHandler logoutHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        // 把codeGenerator生成的授权码长度重定义为8
        OAuth2AuthorizationCodeRequestAuthenticationProvider authProvider =
                new OAuth2AuthorizationCodeRequestAuthenticationProvider(
                        registeredClientRepository,
                        authorizationService,
                        authorizationConsentService
                );
        TokenGenerator.AuthorizationCodeGenerator authorizationCodeGenerator = new TokenGenerator.AuthorizationCodeGenerator();
        authProvider.setAuthorizationCodeGenerator(authorizationCodeGenerator);

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, authorizationServer ->
                        authorizationServer
                                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                                        .authenticationProvider(authProvider))
                                .oidc(oidc -> oidc
                                        .userInfoEndpoint(userInfo -> userInfo
                                                .userInfoMapper(context -> {
                                                    // oidc的用户信息存储在id_token里，所以此处必须从Authorization的token集合里取OidcIdToken
                                                    OAuth2Authorization authorization = context.getAuthorization();
                                                    OAuth2Authorization.Token<OidcIdToken> idToken = authorization.getToken(OidcIdToken.class);
                                                    Map<String, Object> claims = idToken.getClaims();
                                                    return new OidcUserInfo(claims);
                                                })
                                        )
                                )
                )
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(CommonConstant.loginUrl),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests (authorize -> authorize
                        .requestMatchers (
                                new AntPathRequestMatcher ("/oauth2/**"),
                                new AntPathRequestMatcher ("/oauth/**"),
                                new AntPathRequestMatcher ("/userinfo"),
                                new AntPathRequestMatcher ("/api/**"),
                                new AntPathRequestMatcher ("/css/**"),
                                new AntPathRequestMatcher ("/.well-known/**"),
                                new AntPathRequestMatcher ("/error/**")
                        ).permitAll ()
                        .anyRequest ().authenticated ()
                )
                .authenticationProvider(daoAuthenticationProvider())
                .formLogin(x -> x.loginPage(CommonConstant.loginUrl)
                        .loginProcessingUrl(CommonConstant.loginProcessingUrl).permitAll())
                .logout(x -> x.logoutUrl(CommonConstant.logoutUrl)
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessUrl(CommonConstant.loginUrl));

        return http.build();
    }

    @Bean
    protected CustomUserDetailsAuthenticationProvider daoAuthenticationProvider() {
        CustomUserDetailsAuthenticationProvider daoAuthenticationProvider = new CustomUserDetailsAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
