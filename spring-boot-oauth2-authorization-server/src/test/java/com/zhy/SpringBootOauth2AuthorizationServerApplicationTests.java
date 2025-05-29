package com.zhy;

import com.zhy.controller.ApiController;
import com.zhy.vo.RegisteredClientVo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootOauth2AuthorizationServerApplicationTests {
    @Resource
    private ApiController controller;

    @Test
    void contextLoads() {
    }

    @Test
    void save() {
        RegisteredClientVo registeredClientVo = new RegisteredClientVo();
        registeredClientVo.setClientId("oidc-client");
        registeredClientVo.setClientSecret("admin");
        registeredClientVo.setClientName(registeredClientVo.getClientId());
        registeredClientVo.setRedirectUri("https://www.baidu.com");
        String response = controller.save(registeredClientVo);
        System.out.println(response);
    }

}
