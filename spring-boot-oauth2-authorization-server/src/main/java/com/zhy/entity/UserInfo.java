package com.zhy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhy
 * @date 2025/5/16 16:10
 */
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 353775678131203036L;
    private String id;
    private String tenantId;
    private String username;
    private String password;
    private String name;
    private String email;
}
