package com.zhy.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhy.constant.CommonConstant;
import com.zhy.entity.CustomUserMixin;
import com.zhy.service.CustomJdbcOAuth2AuthorizationService;
import com.zhy.entity.CustomUser;
import com.zhy.service.CustomRedirectUriOAuth2AuthenticationValidator;
import com.zhy.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.authentication.OAuth2AuthenticationValidator;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author zhy
 * @date 2022/10/12 9:01
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {
    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        /**
         * 默认方法
         */
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        /**
         * 自定义 OAuth2AuthorizationCodeRequestAuthenticationProvider
         */
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.authorizationEndpoint(authorizationEndpointConfigurer -> {
            OAuth2AuthorizationCodeRequestAuthenticationProvider authenticationProvider =
                    new OAuth2AuthorizationCodeRequestAuthenticationProvider(
                            registeredClientRepository(), authorizationService() , authorizationConsentService());
            authenticationProvider.setAuthenticationValidatorResolver(createDefaultAuthenticationValidatorResolver());
            authorizationEndpointConfigurer.authenticationProvider(authenticationProvider);
        });
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http.requestMatcher(endpointsMatcher).authorizeRequests((authorizeRequests) -> {
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) authorizeRequests.anyRequest()).authenticated();
        }).csrf((csrf) -> {
            csrf.ignoringRequestMatchers(new RequestMatcher[]{endpointsMatcher});
        }).apply(authorizationServerConfigurer);

        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .userDetailsService(userDetailsService)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint(CommonConstant.loginUrl))
                )
                // oidc userinfo endpoint
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        return registeredClientRepository;
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        RegisteredClientRepository registeredClientRepository = registeredClientRepository();
        CustomJdbcOAuth2AuthorizationService service = new CustomJdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository, redisTemplate);
        CustomJdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new CustomJdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);

        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = CustomJdbcOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.addMixIn(CustomUser.class, CustomUserMixin.class);
        authorizationRowMapper.setObjectMapper(objectMapper);

        service.setAuthorizationRowMapper(authorizationRowMapper);
        return service;
    }

    /**
     * 授权确认信息处理服务
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository());
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().build();
    }

    private Function<String, OAuth2AuthenticationValidator> createDefaultAuthenticationValidatorResolver() {
        Map<String, OAuth2AuthenticationValidator> authenticationValidators = new HashMap();
        authenticationValidators.put("redirect_uri", new CustomRedirectUriOAuth2AuthenticationValidator());
        authenticationValidators.put("scope", new CustomRedirectUriOAuth2AuthenticationValidator());
        return authenticationValidators::get;
    }
}
