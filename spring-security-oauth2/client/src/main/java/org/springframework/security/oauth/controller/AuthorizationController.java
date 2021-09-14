/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AuthorizationController {
    @Value("${messages.base-uri}")
    private String messagesBaseUri;

    @Resource(name = "authCodeRestTemplate")
    private OAuth2RestTemplate authCodeRestTemplate;

    @GetMapping(value = "/")
    public String root() {
        return authorizationByAuthCode();
    }

    @GetMapping(value = "/auth", params = "grant_type=authorization_code")
    public String authorizationByAuthCode() {
        String response = authCodeRestTemplate.getForObject(messagesBaseUri, String.class);
        return response;
    }

    @GetMapping(value = "/auth")
    public String authorized() {
        return authorizationByAuthCode();
    }
}