package com.zhy.service;

import com.zhy.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhy
 * @date 2022/10/12 10:01
 */
@Slf4j
public class CustomLogoutHandler  implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (response == null) {
            return;
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
