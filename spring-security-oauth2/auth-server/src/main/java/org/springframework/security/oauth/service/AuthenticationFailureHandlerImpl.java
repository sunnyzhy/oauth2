package org.springframework.security.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.constant.HTTP_HEADER;
import org.springframework.security.oauth.vo.ResponseVo;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zhy
 * @date 2021/3/5 10:18
 */
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    public AuthenticationFailureHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setHeader(HTTP_HEADER.CORS.getKey(), HTTP_HEADER.CORS.getValue());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = httpServletResponse.getWriter();
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(HttpStatus.UNAUTHORIZED.value());
        responseVo.setMsg(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        responseVo.setData(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        writer.write(objectMapper.writeValueAsString(responseVo));
        writer.flush();
        writer.close();
    }
}
