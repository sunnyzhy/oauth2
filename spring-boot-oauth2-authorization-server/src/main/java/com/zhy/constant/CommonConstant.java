package com.zhy.constant;

/**
 * @author zhy
 * @date 2022/10/12 10:45
 */
public class CommonConstant {
    /**
     * 自定义登陆页面(认证)
     */
    public static final String loginUrl = "/oauth/login";
    /**
     * 系统默认的授权地址(授权)
     */
    public static final String loginProcessingUrl = "/oauth/login/authorize";
    public static final String logoutUrl = "/oauth/logout";

    /**
     * token:type:token_value
     */
    public static final String TOKEN_REDIS_KEY_FORMAT = "token:%s:%s";
}
