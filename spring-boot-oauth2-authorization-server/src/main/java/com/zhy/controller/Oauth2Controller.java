package com.zhy.controller;

import com.zhy.constant.CommonConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhy
 * @date 2025/5/16 14:26
 */
@Controller
public class Oauth2Controller {
    @GetMapping(CommonConstant.loginUrl)
    public String loginPage(Model model) {
        model.addAttribute("loginProcessUrl", CommonConstant.loginProcessingUrl);
        return "login";
    }

}
