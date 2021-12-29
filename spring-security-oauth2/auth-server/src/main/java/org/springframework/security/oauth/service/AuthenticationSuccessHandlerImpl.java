package org.springframework.security.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhy
 * @date 2021/3/5 10:04
 */
@Component
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final ClientDetailsService clientDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JdbcClientDetailsService jdbcClientDetailsService;
    private final TokenStore tokenStore;
    private final JwtAccessTokenConverter accessTokenConverter;
    private RequestCache requestCache = new HttpSessionRequestCache();

    public AuthenticationSuccessHandlerImpl(ObjectMapper objectMapper, ClientDetailsService clientDetailsService, PasswordEncoder passwordEncoder, JdbcClientDetailsService jdbcClientDetailsService, TokenStore tokenStore, JwtAccessTokenConverter accessTokenConverter) {
        this.objectMapper = objectMapper;
        this.clientDetailsService = clientDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jdbcClientDetailsService = jdbcClientDetailsService;
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            String targetUrlParameter = this.getTargetUrlParameter();
            if (!this.isAlwaysUseDefaultTargetUrl() && (targetUrlParameter == null || !StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
                this.clearAuthenticationAttributes(request);
                String targetUrl = savedRequest.getRedirectUrl();
                TokenRequestParams tokenRequestParams = analyzeTokenRequest(targetUrl);
                if (tokenRequestParams == null) {
                    this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
                } else {
                    ClientDetails clientDetails = clientDetailsService.loadClientByClientId(tokenRequestParams.getClientId());
                    if (clientDetails == null) {
                        this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
                        return;
                    } else if (!passwordEncoder.matches(tokenRequestParams.getClientSecret(), clientDetails.getClientSecret())) {
                        this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
                        return;
                    }
                    OAuth2AccessToken token = createToken(clientDetails, tokenRequestParams, authentication);
                    String redirectUrl = clientDetails.getRegisteredRedirectUri().iterator().next();
//                    redirectUrl += "?" + objectMapper.writeValueAsString(token);
//                    redirectUrl = response.encodeRedirectURL(redirectUrl);
                    response.sendRedirect(redirectUrl);
                }
            } else {
                this.requestCache.removeRequest(request, response);
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }
    }

    @Data
    class TokenRequestParams {
        private String grantType;
        private String clientId;
        private String clientSecret;
    }

    private TokenRequestParams analyzeTokenRequest(String url) {
        String items[] = url.split("\\?");
        if (items.length < 2) {
            return null;
        }
        if (items[0].indexOf("/token") == -1) {
            return null;
        }
        TokenRequestParams tokenRequestParams = new TokenRequestParams();
        items = items[1].split("&");
        int count = 0;
        for (String item : items) {
            if (item.indexOf("grant_type=") == 0) {
                tokenRequestParams.setGrantType(item.substring("grant_type=".length()));
                count++;
            } else if (item.indexOf("client_id=") == 0) {
                tokenRequestParams.setClientId(item.substring("client_id=".length()));
                count++;
            } else if (item.indexOf("client_secret=") == 0) {
                tokenRequestParams.setClientSecret(item.substring("client_secret=".length()));
                count++;
            }
            if (count == 3) {
                break;
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(tokenRequestParams.getClientId()) ||
                org.apache.commons.lang3.StringUtils.isEmpty(tokenRequestParams.getClientSecret())) {
            return null;
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(tokenRequestParams.getGrantType())) {
            tokenRequestParams.setGrantType("authorization_code");
        }
        return tokenRequestParams;
    }

    private OAuth2AccessToken createToken(ClientDetails clientDetails, TokenRequestParams tokenRequestParams, Authentication authentication) {
        String redirectUrl = clientDetails.getRegisteredRedirectUri().iterator().next();
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_secret", tokenRequestParams.getClientSecret());
        requestParameters.put("redirect_uri", redirectUrl);
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientDetails.getClientId(), clientDetails.getScope(), tokenRequestParams.getGrantType());
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        AuthorizationServerTokenServices tokenServices = createTokenServices();
        OAuth2AccessToken token = tokenServices.createAccessToken(oAuth2Authentication);
        System.out.println(token);
        return token;
    }

    private AuthorizationServerTokenServices createTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(jdbcClientDetailsService);//客户端详情服务
        tokenServices.setSupportRefreshToken(true);//支持刷新令牌
        //service.setReuseRefreshToken(true); // 复用refresh token
        tokenServices.setTokenStore(tokenStore);//令牌存储策略
        tokenServices.setTokenEnhancer(accessTokenConverter);
        return tokenServices;
    }
}
