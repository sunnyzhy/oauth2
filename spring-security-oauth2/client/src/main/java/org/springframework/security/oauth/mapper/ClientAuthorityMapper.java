package org.springframework.security.oauth.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.oauth.model.ClientAuthority;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ClientAuthorityMapper extends Mapper<ClientAuthority> {
    List<ClientAuthority> selectRole(@Param("userId") Integer userId);
}