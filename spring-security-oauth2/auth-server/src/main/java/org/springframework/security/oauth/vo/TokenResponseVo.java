package org.springframework.security.oauth.vo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class TokenResponseVo {
    private JsonNode token;
}
