package com.zhy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zhy
 * @date 2025/5/16 14:13
 */
@Configuration
public class PasswordEncoderBean {
    private final int PW_ENCORDER_SALT = 12;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PW_ENCORDER_SALT);
    }
}
