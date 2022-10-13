package com.zhy.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Set;

/**
 * @author zhy
 * @date 2022/10/12 9:39
 */
public class CustomUserDeserializer extends JsonDeserializer<CustomUser> {

    private static final TypeReference<Set<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<Set<SimpleGrantedAuthority>>() {
    };

    @Override
    public CustomUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Set<? extends GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"),
                SIMPLE_GRANTED_AUTHORITY_SET);
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        String username = readJsonNode(jsonNode, "username").asText();

        String userId = readJsonNode(jsonNode, "userId").asText();
        String email = readJsonNode(jsonNode, "email").asText();
        String phone = readJsonNode(jsonNode, "phone").asText();

        String password = passwordNode.asText("");
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
        boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
        boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
        boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();
        CustomUser result = new CustomUser(userId, email, phone, username, password, authorities);
        if (passwordNode.asText(null) == null) {
            result.eraseCredentials();
        }
        return result;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
