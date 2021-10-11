package org.springframework.security.oauth.condition.cors;

/**
 * @author zhouyi
 * @date 2021/1/25 14:31
 */
public enum CORS_STRATEGY {
    SERVER(0, "SERVER", "服务端实现跨域"),
    NGINX(1, "NGINX", "Nginx代理实现跨域");

    private Integer code;
    private String name;
    private String value;

    CORS_STRATEGY(Integer code, String name, String value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
