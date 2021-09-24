package org.springframework.security.oauth.controller;

import org.springframework.security.oauth.service.AuthService;
import org.springframework.security.oauth.vo.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public ResponseVo<LoginResponseVo> login(@RequestBody LoginRequestVo loginRequestVo) {
        ResponseVo<LoginResponseVo> responseVo = authService.login(loginRequestVo);
        return responseVo;
    }

    @PostMapping(value = "/code")
    public ResponseVo<CodeResponseVo> code(@RequestBody CodeRequestVo codeRequestVo) {
        ResponseVo<CodeResponseVo> responseVo = authService.getCode(codeRequestVo);
        return responseVo;
    }

    @PostMapping(value = "/token")
    public ResponseVo<TokenResponseVo> token(@RequestBody TokenRequestVo tokenRequestVo) {
        ResponseVo<TokenResponseVo> responseVo = authService.getToken(tokenRequestVo);
        return responseVo;
    }
}
