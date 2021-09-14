package org.springframework.security.oauth.constant;

/**
 * @author zhy
 * @date 2021/3/11 14:37
 */
public enum HTTP_HEADER {
    CORS("Access-Control-Allow-Origin", "*"),
    CONTENT_TYPE("Content-Type", "application/json;charset=utf-8");

    private String key;
    private String value;

    HTTP_HEADER(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
