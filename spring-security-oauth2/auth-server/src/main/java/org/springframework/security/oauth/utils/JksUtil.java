package org.springframework.security.oauth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth.model.Jks;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhouyi
 * @date 2021/1/11 16:14
 */
@Component
public class JksUtil {
    private final ObjectMapper objectMapper;

    public JksUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 返回私钥签名后的token
     * @param jks
     * @param contentMap
     * @return
     * @throws JsonProcessingException
     */
    public String encode(Jks jks, Map<String, String> contentMap) throws JsonProcessingException {
        String bodyString = objectMapper.writeValueAsString(contentMap);
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(jks.getPrivateKey()));
        String content = jwt.getEncoded();
        return content;
    }

    /**
     * 使用公钥验证签名，并返回原始数据
     * @param jks
     * @param token
     * @return
     */
    public String decode(Jks jks, String token) {
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(jks.getPublicKey()));
        String claims = jwt.getClaims();
        return claims;
    }
}
