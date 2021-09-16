package org.springframework.security.oauth.mapper;

import org.springframework.security.oauth.model.ClientUser;
import tk.mybatis.mapper.common.Mapper;

public interface ClientUserMapper extends Mapper<ClientUser> {
    ClientUser selectUser(String username);
}