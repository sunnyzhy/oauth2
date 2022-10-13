package com.zhy.entity;

import lombok.Data;

import java.util.Set;

/**
 * @author zhy
 * @date 2022/10/13 11:46
 */
@Data
public class RegisteredClientVo {
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String redirectUri;
    private Set<String> scopes;
}
