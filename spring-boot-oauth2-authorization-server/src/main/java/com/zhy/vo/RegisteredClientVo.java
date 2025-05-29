package com.zhy.vo;

import lombok.Data;

/**
 * @author zhy
 * @date 2025/5/29 10:50
 */
@Data
public class RegisteredClientVo {
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String redirectUri;
}
