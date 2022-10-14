package com.zhy.controller;

import com.zhy.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
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
    private final RedisTemplate<String, Object> redisTemplate;

    public OAuth2OidcController(JwtDecoder jwtDecoder, RedisTemplate<String, Object> redisTemplate) {
        this.jwtDecoder = jwtDecoder;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping(value = "/oauth/userinfo")
    public Map<String, Object> userInfo(HttpServletRequest request) {
        Map<String, Object> claims = new HashMap<>();
        String header = request.getHeader("Authorization");
        if (StringUtils.isEmpty(header)) {
            return claims;
        }
        String idToken = StringUtils.substringAfter(header, "Bearer ");
        if (StringUtils.isEmpty(idToken)) {
            return claims;
        }
        String key = String.format(CommonConstant.TOKEN_REDIS_KEY_FORMAT, OidcParameterNames.ID_TOKEN, idToken);
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
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
