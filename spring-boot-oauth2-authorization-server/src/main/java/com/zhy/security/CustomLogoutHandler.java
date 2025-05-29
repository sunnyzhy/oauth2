package com.zhy.security;

import com.zhy.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zhy
 * @date 2025/5/16 14:38
 */
@Slf4j
@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Autowired
    private OAuth2AuthorizationService authorizationService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("access-token");
        if (StringUtils.isNotEmpty(token)) {
            OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            if (authorization != null) {
                authorizationService.remove(authorization);
            }
        }
        // redirect即为前端传来自定义跳转url地址
        String redirect = request.getParameter("redirect");
        redirect = !StringUtils.isEmpty(redirect) ? redirect : CommonConstant.loginUrl;
        try {
            // 实现自定义重定向
            response.sendRedirect(redirect);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
