package org.springframework.security.oauth.condition.separation;

/**
 * @author zhouyi
 * @date 2021/1/25 14:31
 */
public enum FRONT_BACK_SEPARATION {
    BACK_END(0, "back_end", "前后端不分离"),
    FRONT_END(1, "front_end", "前后端分离");

    private Integer code;
    private String name;
    private String value;

    FRONT_BACK_SEPARATION(Integer code, String name, String value) {
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
