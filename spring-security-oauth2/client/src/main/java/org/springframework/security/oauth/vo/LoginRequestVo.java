package org.springframework.security.oauth.vo;

import lombok.Data;

@Data
public class LoginRequestVo {
    private String username;
    private String password;
}
