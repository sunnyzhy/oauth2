package org.springframework.security.oauth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@Configuration
@EnableOAuth2Client
public class ClientConfig {
    @ConfigurationProperties(prefix = "security.oauth2.client.client-auth-code")
    @Bean(name = "authCodeResourceDetails")
    public OAuth2ProtectedResourceDetails authCodeResourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean(name = "authCodeRestTemplate")
    public OAuth2RestTemplate authCodeRestTemplate(
            @Qualifier("authCodeResourceDetails") OAuth2ProtectedResourceDetails authCodeResourceDetails,
            OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(authCodeResourceDetails, oauth2ClientContext);
    }
}