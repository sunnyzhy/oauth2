package org.springframework.security.oauth.controller;

import org.springframework.security.oauth.config.OAuthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthController {
    private final OAuthConfig oAuthConfig;

    public OAuthController(OAuthConfig oAuthConfig) {
        this.oAuthConfig = oAuthConfig;
    }

    @GetMapping(value = "/oauth/login")
    public String loginPage(Model model){
        model.addAttribute("loginUrl",oAuthConfig.getLoginProcessingUrl());
        return "login";
    }
}
