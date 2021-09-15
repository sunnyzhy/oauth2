package org.springframework.security.oauth.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * security.userdetails 接口的实现类
 *
 * @author zhouyi
 * @date 2021/1/12 11:07
 */
@Data
public class UserDetailsImpl implements UserDetails, Serializable {
    private static final long serialVersionUID = 4125096758372084309L;
    private Integer id;
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        for (String role : roles) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
            list.add(grantedAuthority);
        }
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
