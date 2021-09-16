package org.springframework.security.oauth.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final JdbcClientDetailsService jdbcClientDetailsService;

    public AuthenticationEntryPointImpl(JdbcClientDetailsService jdbcClientDetailsService) {
        this.jdbcClientDetailsService = jdbcClientDetailsService;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 1. 重定向到自定义的认证接口
        // 2. 输入 client_id
        // 3. ClientDetails client = jdbcClientDetailsService.loadClientByClientId(client_id);
        // 4. 重定向到地址(需要拼接) "http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost"
        httpServletResponse.sendRedirect("http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost");
    }
}
