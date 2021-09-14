package org.springframework.security.oauth.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhy
 * @date 2021/3/5 10:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVo<T> implements Serializable {
    Integer code;
    String msg;
    T data;
}