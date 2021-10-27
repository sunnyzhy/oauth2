package org.springframework.security.oauth.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth.condition.cors.ServerCorsCondition;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author zhouyi
 * @date 2021/1/12 15:07
 */
@Data
@Configuration
//@ConfigurationProperties(prefix = "cors.allowed")
//@Conditional(ServerCorsCondition.class)
public class CorsConfig {
//    private List<String> originsUrl;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置允许跨域访问的 URL
//        config.setAllowedOrigins(originsUrl);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}