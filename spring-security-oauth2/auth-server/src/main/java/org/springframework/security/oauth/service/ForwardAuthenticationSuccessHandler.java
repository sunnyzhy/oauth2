package org.springframework.security.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.constant.HTTP_HEADER;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
//        Map<String, Object> map = new HashMap<>();
//        map.put("msg", "登录成功！");
//        map.put("principal", authentication.getPrincipal());
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        // 对象转json传输给前端
//        out.write(new ObjectMapper().writeValueAsString(map));
//        out.flush();
//        out.close();
    }
}
