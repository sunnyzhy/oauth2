package org.springframework.security.oauth.vo;

import lombok.Data;

@Data
public class TokenRequestVo {
    private String clientId;
    private String clientSecret;
    private String code;
}
