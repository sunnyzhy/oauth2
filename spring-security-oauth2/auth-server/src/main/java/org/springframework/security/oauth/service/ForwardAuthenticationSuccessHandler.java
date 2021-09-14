package org.springframework.security.oauth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.constant.HTTP_HEADER;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhy
 * @date 2021/3/11 15:14
 */
@Component
public class ForwardAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
        response.setHeader(HTTP_HEADER.CORS.getKey(), HTTP_HEADER.CORS.getValue());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
