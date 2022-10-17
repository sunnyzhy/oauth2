package com.zhy.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhy
 * @date 2022/10/12 9:33
 */
@Getter
public class CustomUser extends User implements OAuth2AuthenticatedPrincipal {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private String userId;
    private String profile;
    private String email;
    private String address;
    private String phone;

    public CustomUser(String userId, String profile, String email, String address, String phone, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.profile = profile;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

    @Override
    public String getName() {
        return this.getUsername();
    }
}
