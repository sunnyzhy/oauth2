/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.annotation.Resource;

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