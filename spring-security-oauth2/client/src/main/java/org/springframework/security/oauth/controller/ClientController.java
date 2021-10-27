package org.springframework.security.oauth.controller;

import org.springframework.security.oauth.util.ResponseVoUtil;
import org.springframework.security.oauth.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/client")
public class ClientController {
    @GetMapping(value = "/info")
    public ResponseVo<String> info(){
        return ResponseVoUtil.success("hello");
    }
}
