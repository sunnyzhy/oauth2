package org.springframework.security.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth.condition.JdbcCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouyi
 * @date 2021/1/25 10:40
 */
@Component
@Slf4j
@Conditional(JdbcCondition.class)
public class TokenEnhancerImpl implements TokenEnhancer {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        Object principal = authentication.getPrincipal();
        try {
            String payload = objectMapper.writeValueAsString(principal);
            Map map = objectMapper.readValue(payload, Map.class);
            map.remove("password");
            map.put("zzz", "000000");
            additionalInfo.put("user_info", map);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return accessToken;
    }
}
