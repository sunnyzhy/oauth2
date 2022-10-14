package com.zhy.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhy
 * @date 2022/10/12 9:16
 */
@RestController
@Slf4j
public class OAuth2OidcController {
    private final JwtDecoder jwtDecoder;

    public OAuth2OidcController(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @GetMapping(value = "/oauth/userinfo")
    public Map<String, Object> info(HttpServletRequest request) {
        Map<String, Object> claims = new HashMap<>();
        String header = request.getHeader("Authorization");
        String idToken = StringUtils.substringAfter(header, "Bearer ");
        if (StringUtils.isEmpty(idToken)) {
            return claims;
        }
        try {
            Jwt jwt = jwtDecoder.decode(idToken);
            claims.putAll(jwt.getClaims());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return claims;
    }
}
