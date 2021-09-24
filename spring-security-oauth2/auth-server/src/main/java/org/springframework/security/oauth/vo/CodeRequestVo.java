package org.springframework.security.oauth.vo;

import lombok.Data;

@Data
public class CodeRequestVo {
    private String clientId;
    private String cookie;
}
