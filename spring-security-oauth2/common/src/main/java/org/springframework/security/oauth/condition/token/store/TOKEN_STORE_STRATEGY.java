package org.springframework.security.oauth.condition.token.store;

/**
 * @author zhouyi
 * @date 2021/1/25 14:31
 */
public enum TOKEN_STORE_STRATEGY {
    MEMORY(0, "memory", "InMemoryTokenStore"),
    JDBC(1, "jdbc", "JdbcTokenStore"),
    JWT(2, "jwt", "JwtTokenStore"),
    REDIS(3, "redis", "RedisTokenStore");

    private Integer code;
    private String name;
    private String value;

    TOKEN_STORE_STRATEGY(Integer code, String name, String value) {
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
