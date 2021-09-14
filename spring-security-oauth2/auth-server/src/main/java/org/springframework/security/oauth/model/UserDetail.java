package org.springframework.security.oauth.model;

import lombok.Data;
import org.springframework.security.oauth.entity.OauthExtendUserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装的数据库实体类，包含用户名、密码、授权的角色
 *
 * @author zhy
 * @date 2021/2/23 10:17
 */
@Data
public class UserDetail extends OauthExtendUserDetails implements Serializable {
    private List<String> roles = new ArrayList<>();
}
