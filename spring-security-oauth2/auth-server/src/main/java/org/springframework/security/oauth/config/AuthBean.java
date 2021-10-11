package org.springframework.security.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth.service.AuthenticationProviderImpl;

import java.util.Arrays;

@Configuration
public class AuthBean {
    private final AuthenticationProviderImpl authenticationProvider;

    public AuthBean(AuthenticationProviderImpl authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        // 指定authenticationManager的成员是自定义AuthenticationProviderImpl的，否则会因为用户名、密码错误而再调用默认的DaoAuthenticationProvider，从而抛出"UserDetailsService returned null, which is an interface contract violation"
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(authenticationProvider));
        //不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
//        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }
}
