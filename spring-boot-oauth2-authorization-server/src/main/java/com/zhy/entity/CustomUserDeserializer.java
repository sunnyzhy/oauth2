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

import java.io.IOException;
import java.util.Set;

/**
 * @author zhy
 * @date 2025/5/16 14:14
 */
public class CustomUserDeserializer extends JsonDeserializer<CustomUser> {

    private static final TypeReference<Set<GrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<Set<GrantedAuthority>>() {
    };


    @Override
    public CustomUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Set<GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"), SIMPLE_GRANTED_AUTHORITY_SET);
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        String username = readJsonNode(jsonNode, "username").asText();
        String password = passwordNode.asText("");
        String tenantId = readJsonNode(jsonNode, "tenantId").asText();
        String name = readJsonNode(jsonNode, "name").asText();
        String email = readJsonNode(jsonNode, "email").asText();
        CustomUser user = new CustomUser(username, password, authorities);
        user.setTenantId(tenantId);
        user.setName(name);
        user.setEmail(email);
        if (passwordNode.asText(null) == null) {
            user.eraseCredentials();
        }
        return user;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
