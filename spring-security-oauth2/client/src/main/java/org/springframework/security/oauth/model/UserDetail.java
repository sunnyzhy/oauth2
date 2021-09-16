package org.springframework.security.oauth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装的数据库实体类，包含用户名、密码、授权的角色
 *
 * @author zhouyi
 * @date 2021/1/12 10:55
 */
@Data
public class UserDetail extends ClientUser implements Serializable {
    private List<String> roles = new ArrayList<>();
}
