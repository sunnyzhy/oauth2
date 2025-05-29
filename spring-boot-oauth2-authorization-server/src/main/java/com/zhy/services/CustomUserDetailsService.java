package com.zhy.services;

import com.zhy.entity.CustomUser;
import com.zhy.entity.UserInfo;
import com.zhy.util.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author zhy
 * @date 2025/5/16 14:45
 */
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        // 注：
        // 1. 从业务端获取UserInfo
        // 2. 业务端返回非成功消息时，此处必须抛出InternalAuthenticationServiceException才能被Oauth2响应
        // throwException("xxxxxx")
        UserInfo userInfo = new UserInfo();
        userInfo.setId(StringUtil.uuidTrim());
        userInfo.setUsername("admin");
        userInfo.setName("admin");
        userInfo.setTenantId(StringUtil.uuidTrim());
        userInfo.setEmail("123@163.com");
        userInfo.setPassword(passwordEncoder.encode("admin"));
        return initUserDetails(userInfo);
    }

    private CustomUser initUserDetails(UserInfo userInfo) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        CustomUser user = new CustomUser(userInfo.getUsername(), userInfo.getPassword(), authorities);
        user.setTenantId(userInfo.getTenantId());
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        return user;
    }

    public void throwException(String msg) {
        throw new InternalAuthenticationServiceException(msg);
    }
}
