package org.springframework.security.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.constant.HTTP_HEADER;
import org.springframework.security.oauth.vo.ResponseVo;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zhy
 * @date 2021/3/5 10:04
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    public AuthenticationSuccessHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setHeader(HTTP_HEADER.CORS.getKey(), HTTP_HEADER.CORS.getValue());
        httpServletResponse.setHeader(HTTP_HEADER.CONTENT_TYPE.getKey(), HTTP_HEADER.CONTENT_TYPE.getValue());
        httpServletResponse.setStatus(200);
        PrintWriter writer = httpServletResponse.getWriter();
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(0);
        responseVo.setMsg("success");
        writer.write(objectMapper.writeValueAsString(responseVo));
        writer.flush();
        writer.close();
    }
}
