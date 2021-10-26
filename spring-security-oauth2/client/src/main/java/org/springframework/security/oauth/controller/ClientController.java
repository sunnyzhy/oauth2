package org.springframework.security.oauth.controller;

import org.springframework.security.oauth.util.ResponseVoUtil;
import org.springframework.security.oauth.vo.LoginRequestVo;
import org.springframework.security.oauth.vo.LoginResponseVo;
import org.springframework.security.oauth.vo.ResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/client")
public class ClientController {
    @PostMapping(value = "/login")
    public ResponseVo<LoginResponseVo> login(@RequestBody LoginRequestVo loginRequestVo) {
        return ResponseVoUtil.success();
    }
}
