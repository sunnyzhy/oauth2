package org.springframework.security.oauth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AuthorizationController {
    @Value("${messages.base-uri}")
    private String messagesBaseUri;

    @Resource(name = "authCodeRestTemplate")
    private OAuth2RestTemplate authCodeRestTemplate;

    @GetMapping(value = "/authorized")
    public String authorized() {
        String response = authCodeRestTemplate.getForObject(messagesBaseUri, String.class);
        return response;
    }
}