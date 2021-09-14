package org.springframework.security.oauth.config;

import org.springframework.security.oauth.condition.JdbcCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@Conditional(JdbcCondition.class)
public class JdbcBeanConfig {
    @Resource
    private DataSource dataSource;

    /**
     * table: oauth_approvals
     * <p>
     * 保存授权信息
     * 如果 oauth_approvals 表里保存了 userId 和 clientId 对应的授权信息，就会跳过授权页面而直接返回授权码 code
     * 可以结合 ApprovalStoreUserApprovalHandler 策略使用 ApprovalStore
     *
     * @return
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

}
