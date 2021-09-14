package org.springframework.security.oauth.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.oauth.entity.OauthExtendAuthority;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OauthExtendAuthorityMapper extends Mapper<OauthExtendAuthority> {
    List<OauthExtendAuthority> selectRole(@Param("userId") Integer userId);
}