package org.springframework.security.oauth.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.oauth.mapper.ClientAuthorityMapper;
import org.springframework.security.oauth.mapper.ClientUserMapper;
import org.springframework.security.oauth.model.ClientAuthority;
import org.springframework.security.oauth.model.ClientUser;
import org.springframework.security.oauth.model.UserDetail;
import org.springframework.security.oauth.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhouyi
 * @date 2021/1/12 10:57
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientUserMapper userMapper;
    private final ClientAuthorityMapper clientAuthorityMapper;

    public UserDetailsServiceImpl(ClientUserMapper userMapper, ClientAuthorityMapper clientAuthorityMapper) {
        this.userMapper = userMapper;
        this.clientAuthorityMapper = clientAuthorityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库的实体类
        ClientUser user = userMapper.selectUser(username);
        if (user == null) {
            return null;
        }
        // 封装数据库的实体类
        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(user, userDetail);
        // 封装授权的角色
        List<ClientAuthority> authorityList = clientAuthorityMapper.selectRole(userDetail.getId());
        for (ClientAuthority authority : authorityList) {
            userDetail.getRoles().add(authority.getRole());
        }
        return new UserDetailsImpl(userDetail);
    }
}
