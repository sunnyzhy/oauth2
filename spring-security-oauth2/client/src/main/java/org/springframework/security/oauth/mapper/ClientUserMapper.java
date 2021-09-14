package org.springframework.security.oauth.mapper;

import org.springframework.security.oauth.entity.ClientUser;
import tk.mybatis.mapper.common.Mapper;

public interface ClientUserMapper extends Mapper<ClientUser> {
    ClientUser selectUser(String username);
}