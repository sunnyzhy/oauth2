package org.springframework.security.oauth.vo;

import lombok.Data;

@Data
public class LoginResponseVo {
    private String username;
    private String cookie;
}
