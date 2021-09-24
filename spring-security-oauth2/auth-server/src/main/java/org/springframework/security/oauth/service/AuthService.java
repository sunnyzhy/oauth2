package org.springframework.security.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth.util.ResponseVoUtil;
import org.springframework.security.oauth.vo.*;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AuthService {
    @Value("${login.interface-url}")
    private String loginUrl;
    @Value("${auth.authorization-uri}")
    private String authorizationUrl;
    @Value("${auth.access-token-uri}")
    private String accessTokenUri;
    private final JdbcClientDetailsService jdbcClientDetailsService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public AuthService(JdbcClientDetailsService jdbcClientDetailsService, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.jdbcClientDetailsService = jdbcClientDetailsService;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public ResponseVo<LoginResponseVo> login(LoginRequestVo loginRequestVo) {
        if (StringUtils.isBlank(loginRequestVo.getUsername()) ||
                StringUtils.isBlank(loginRequestVo.getPassword())) {
            return ResponseVoUtil.error(-1, "parameter error!");
        }
        HttpHeaders headers = new HttpHeaders();
        // 以表单的形式提交数据
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 封装用户名、密码
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", loginRequestVo.getUsername());
        map.add("password", loginRequestVo.getPassword());
        HttpEntity<MultiValueMap<String, Object>> param = new HttpEntity<>(map, headers);
        // 调用 spring security 的 /login 接口
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(loginUrl, param, String.class);
        } catch (RestClientException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseVoUtil.error(-1, "login failed!");
        }
        if (response == null) {
            return ResponseVoUtil.error(-1, "login failed!");
        }
        // 提取 cookie
        List<String> cookieList = response.getHeaders().get("Set-Cookie");
        if (cookieList == null && cookieList.isEmpty()) {
            return ResponseVoUtil.error(-1, "login failed!");
        }
        String cookie = cookieList.get(0);
        cookie = cookie.split(";")[0];

        LoginResponseVo loginResponseVo = new LoginResponseVo();
        loginResponseVo.setUsername(loginRequestVo.getUsername());
        loginResponseVo.setCookie(cookie);
        return ResponseVoUtil.success(loginResponseVo);
    }

    public ResponseVo<CodeResponseVo> getCode(CodeRequestVo codeRequestVo) {
        if (StringUtils.isBlank(codeRequestVo.getClientId()) ||
                StringUtils.isBlank(codeRequestVo.getCookie())) {
            return ResponseVoUtil.error(-1, "parameter error!");
        }
        // 查询 ClientDetails
        ClientDetails clientDetails = null;
        try {
            clientDetails = jdbcClientDetailsService.loadClientByClientId(codeRequestVo.getClientId());
        } catch (InvalidClientException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseVoUtil.error(-1, "fetch client failed!");
        }
        if (clientDetails == null) {
            return ResponseVoUtil.error(-1, "invalid client_id");
        }
        String url = String.format("%s?response_type=code&client_id=%s&redirect_uri=%s", authorizationUrl, clientDetails.getClientId(), clientDetails.getRegisteredRedirectUri().iterator().next());

//        // 重定向
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setRedirectStrategy(new LaxRedirectStrategy())
//                .build();
//        factory.setHttpClient(httpClient);
//        restTemplate.setRequestFactory(factory);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        // 禁用重定向
        HttpClient httpClient = HttpClientBuilder.create()
                .disableRedirectHandling()
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        HttpHeaders requestHeaders = new HttpHeaders();
        // 设置 cookie
        requestHeaders.put("Cookie", Arrays.asList(codeRequestVo.getCookie()));
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        // 调用 oauth2 的 /oauth/authorize 接口
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (RestClientException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseVoUtil.error(-1, "fetch code failed!");
        }
        if (response == null) {
            return ResponseVoUtil.error(-1, "fetch code failed!");
        }
        // 提取重定向地址
        List<String> locationList = response.getHeaders().get("Location");
        if (locationList == null && locationList.isEmpty()) {
            return ResponseVoUtil.error(-1, "fetch code failed!");
        }
        CodeResponseVo codeResponseVo = new CodeResponseVo();
        codeResponseVo.setClientId(clientDetails.getClientId());
        String location = locationList.get(0);
        if (location.indexOf("/auth/login") > 0) {
            codeResponseVo.setCode("-2");
            return ResponseVoUtil.success(codeResponseVo);
        }
        // 提取授权码
        int codeIndex = location.indexOf("?code=");
        if (codeIndex < 0) {
            return ResponseVoUtil.error(-1, "response data error!");
        }
        String code = location.substring(codeIndex + 6);
        codeResponseVo.setCode(code);
        return ResponseVoUtil.success(codeResponseVo);
    }

    public ResponseVo<TokenResponseVo> getToken(TokenRequestVo tokenRequestVo) {
        if (StringUtils.isBlank(tokenRequestVo.getClientId()) ||
                StringUtils.isBlank(tokenRequestVo.getClientSecret()) ||
                StringUtils.isBlank(tokenRequestVo.getCode())) {
            return ResponseVoUtil.error(-1, "parameter error!");
        }
        // 查询 ClientDetails
        ClientDetails clientDetails = null;
        try {
            clientDetails = jdbcClientDetailsService.loadClientByClientId(tokenRequestVo.getClientId());
        } catch (InvalidClientException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseVoUtil.error(-1, "fetch client failed!");
        }
        if (clientDetails == null) {
            return ResponseVoUtil.error(-1, "invalid client_id");
        }
        // 封装请求的参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("grant_type", "authorization_code");
        paramsMap.set("code", tokenRequestVo.getCode());
        paramsMap.set("client_id", tokenRequestVo.getClientId());
        paramsMap.set("client_secret", tokenRequestVo.getClientSecret());
        paramsMap.set("redirect_uri", clientDetails.getRegisteredRedirectUri().iterator().next());
        // 调用 oauth2 的 /oauth/token 接口
        String response = null;
        try {
            response = restTemplate.postForObject(accessTokenUri, paramsMap, String.class);
        } catch (RestClientException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseVoUtil.error(-1, "fetch token failed!");
        }
        if (response == null) {
            return ResponseVoUtil.error(-1, "fetch token failed!");
        }
        // 提取 token
        TokenResponseVo tokenResponseVo = new TokenResponseVo();
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            tokenResponseVo.setToken(jsonNode);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            tokenResponseVo.setToken(null);
        }
        return ResponseVoUtil.success(tokenResponseVo);
    }


}
