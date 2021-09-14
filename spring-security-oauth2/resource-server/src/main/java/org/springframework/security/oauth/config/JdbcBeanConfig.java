//package org.springframework.security.oauth.config;
//
//import org.springframework.security.oauth.condition.JdbcCondition;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
///**
// * @author zhouyi
// * @date 2021/1/11 14:56
// */
//@Configuration
//@Conditional(JdbcCondition.class)
//public class JdbcBeanConfig {
//    @Resource
//    private DataSource dataSource;
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }
//}
