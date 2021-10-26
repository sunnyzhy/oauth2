package org.springframework.security.oauth.config;

import lombok.Data;

@Data
public class OAuthConfig {
    private String[] permit;
    private String loginPage;
    private String loginProcessingUrl;
}
