package org.springframework.security.oauth.config.token.store.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.oauth.condition.token.store.RedisCondition;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@Conditional(RedisCondition.class)
public class RedisBean {
    @Resource
    private DataSource dataSource;
    private final LettuceConnectionFactory lettuceConnectionFactory;

    public RedisBean(LettuceConnectionFactory lettuceConnectionFactory) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
    }

    /**
     * table: oauth_client_details
     * <p>
     * 如果 autoapprove 值是 true，就会直接返回授权码 code；否则，就会打开授权页面
     *
     * @return
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * jwt 生成的 token 只在前、后端之间传输而无须存储，后端需要检验 token 的合法性
     * 注意：刷新 token 的时候
     * 1. 每次刷新 token 都会重新生成 access_token 和 refresh_token
     * 2. 这一种刷新机制与 jdbc 不同，在 jdbc 模式下，如果 refresh_token 没有失效，则只会重新生成 access_token；否则，会重新生成 access_token 和 refresh_token，并且 oauth_access_token.refresh_token = oauth_refresh_token.token_id
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(lettuceConnectionFactory);
    }

}
