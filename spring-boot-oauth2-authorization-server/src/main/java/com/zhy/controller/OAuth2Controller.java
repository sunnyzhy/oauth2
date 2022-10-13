package com.zhy.controller;

import com.zhy.constant.CommonConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhy
 * @date 2022/10/12 9:16
 */
@Controller
public class OAuth2Controller {
    @GetMapping(value = CommonConstant.loginUrl)
    public String loginPage(Model model) {
        model.addAttribute("loginProcessUrl", CommonConstant.loginProcessingUrl);
        return "login";
    }
}
