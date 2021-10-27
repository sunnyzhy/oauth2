package org.springframework.security.oauth.controller;

import org.springframework.security.oauth.util.ResponseVoUtil;
import org.springframework.security.oauth.vo.LoginRequestVo;
import org.springframework.security.oauth.vo.LoginResponseVo;
import org.springframework.security.oauth.vo.ResponseVo;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Resource(name = "authCodeRestTemplate")
    private OAuth2RestTemplate authCodeRestTemplate;

    @PostMapping(value = "/login")
    public ResponseVo<LoginResponseVo> login(@RequestBody LoginRequestVo loginRequestVo) {
        return ResponseVoUtil.success();
    }

    @GetMapping(value = "/token")
    public String authorized() {
        String response = authCodeRestTemplate.getAccessToken().getValue();
        return response;
    }
}