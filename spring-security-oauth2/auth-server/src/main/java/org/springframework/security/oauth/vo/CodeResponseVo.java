package org.springframework.security.oauth.vo;

import lombok.Data;

@Data
public class CodeResponseVo {
    private String clientId;
    private String cookie;
    private String code;
}
