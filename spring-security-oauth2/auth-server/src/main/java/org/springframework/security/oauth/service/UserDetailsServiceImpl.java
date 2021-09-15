package org.springframework.security.oauth.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth.mapper.OauthExtendAuthorityMapper;
import org.springframework.security.oauth.mapper.OauthExtendUserDetailsMapper;
import org.springframework.security.oauth.entity.OauthExtendAuthority;
import org.springframework.security.oauth.entity.OauthExtendUserDetails;
import org.springframework.security.oauth.model.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhouyi
 * @date 2021/1/12 10:57
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final OauthExtendUserDetailsMapper userDetailsMapper;
    private final OauthExtendAuthorityMapper authorityMapper;

    public UserDetailsServiceImpl(OauthExtendUserDetailsMapper userDetailsMapper, OauthExtendAuthorityMapper authorityMapper) {
        this.userDetailsMapper = userDetailsMapper;
        this.authorityMapper = authorityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 根据用户名查询数据库的实体类
        OauthExtendUserDetails user = userDetailsMapper.selectUser(username);
        if (user == null) {
            return null;
        }
        // 封装数据库的实体类
        UserDetailsImpl userDetail = new UserDetailsImpl();
        BeanUtils.copyProperties(user, userDetail);
        // 封装授权的角色
        List<OauthExtendAuthority> authorityList = authorityMapper.selectRole(userDetail.getId());
        for (OauthExtendAuthority authority : authorityList) {
            userDetail.getRoles().add(authority.getRole());
        }
        return userDetail;
    }
}
