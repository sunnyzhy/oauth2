package org.springframework.security.oauth.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.oauth.entity.OauthExtendUserDetails;
import tk.mybatis.mapper.common.Mapper;

public interface OauthExtendUserDetailsMapper extends Mapper<OauthExtendUserDetails> {
    OauthExtendUserDetails selectUser(@Param("username") String username);
}