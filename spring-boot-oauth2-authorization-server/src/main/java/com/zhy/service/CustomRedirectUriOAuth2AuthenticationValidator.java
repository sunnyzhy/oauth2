package com.zhy.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.authentication.OAuth2AuthenticationContext;
import org.springframework.security.oauth2.core.authentication.OAuth2AuthenticationValidator;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;

/**
 * @author zhy
 * @date 2022/10/25 9:17
 *
 * 自定义 redirectUri 的验证逻辑
 *
 * 注意: 标准 oauth2.1 里的 redirectUri 是精确匹配
 *
 */
public class CustomRedirectUriOAuth2AuthenticationValidator implements OAuth2AuthenticationValidator {
    public CustomRedirectUriOAuth2AuthenticationValidator() {
    }

    @Override
    public void validate(OAuth2AuthenticationContext authenticationContext) {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken) authenticationContext.getAuthentication();
        RegisteredClient registeredClient = (RegisteredClient) authenticationContext.get(RegisteredClient.class);
        String requestedRedirectUri = authorizationCodeRequestAuthentication.getRedirectUri();
        if (StringUtils.hasText(requestedRedirectUri)) {
            UriComponents requestedRedirect = null;

            try {
                requestedRedirect = UriComponentsBuilder.fromUriString(requestedRedirectUri).build();
            } catch (Exception var11) {
            }

            if (requestedRedirect == null || requestedRedirect.getFragment() != null) {
                throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
            }

            String requestedRedirectHost = requestedRedirect.getHost();
            if (requestedRedirectHost == null || requestedRedirectHost.equals("localhost")) {
                OAuth2Error error = new OAuth2Error("invalid_request", "localhost is not allowed for the redirect_uri (" + requestedRedirectUri + "). Use the IP literal (127.0.0.1) instead.", "https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-01#section-9.7.1");
                throwError((OAuth2Error) error, "redirect_uri", authorizationCodeRequestAuthentication, registeredClient, (OAuth2AuthorizationRequest) null);
            }

            if (!isLoopbackAddress(requestedRedirectHost)) {
                /**
                 * 自定义 redirectUri 的验证逻辑 - 前缀匹配
                 */
                boolean matched = false;
                for (String redirectUri : registeredClient.getRedirectUris()) {
                    if (requestedRedirectUri.startsWith(redirectUri)) {
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
                }
            } else {
                boolean validRedirectUri = false;
                Iterator var8 = registeredClient.getRedirectUris().iterator();

                while (var8.hasNext()) {
                    String registeredRedirectUri = (String) var8.next();
                    UriComponentsBuilder registeredRedirect = UriComponentsBuilder.fromUriString(registeredRedirectUri);
                    registeredRedirect.port(requestedRedirect.getPort());
                    if (registeredRedirect.build().toString().equals(requestedRedirect.toString())) {
                        validRedirectUri = true;
                        break;
                    }
                }

                if (!validRedirectUri) {
                    throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
                }
            }
        } else if (authorizationCodeRequestAuthentication.getScopes().contains("openid") || registeredClient.getRedirectUris().size() != 1) {
            throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
        }

    }

    private static boolean isLoopbackAddress(String host) {
        if (!"[0:0:0:0:0:0:0:1]".equals(host) && !"[::1]".equals(host)) {
            String[] ipv4Octets = host.split("\\.");
            if (ipv4Octets.length != 4) {
                return false;
            } else {
                try {
                    int[] address = new int[ipv4Octets.length];

                    for (int i = 0; i < ipv4Octets.length; ++i) {
                        address[i] = Integer.parseInt(ipv4Octets[i]);
                    }

                    return address[0] == 127 && address[1] >= 0 && address[1] <= 255 && address[2] >= 0 && address[2] <= 255 && address[3] >= 1 && address[3] <= 255;
                } catch (NumberFormatException var4) {
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    private static void throwError(String errorCode, String parameterName, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient) {
        throwError((String) errorCode, parameterName, authorizationCodeRequestAuthentication, registeredClient, (OAuth2AuthorizationRequest) null);
    }

    private static void throwError(String errorCode, String parameterName, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
        throwError(errorCode, parameterName, "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1", authorizationCodeRequestAuthentication, registeredClient, authorizationRequest);
    }

    private static void throwError(String errorCode, String parameterName, String errorUri, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throwError(error, parameterName, authorizationCodeRequestAuthentication, registeredClient, authorizationRequest);
    }

    private static void throwError(OAuth2Error error, String parameterName, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
        boolean redirectOnError = true;
        if (error.getErrorCode().equals("invalid_request") && (parameterName.equals("client_id") || parameterName.equals("redirect_uri") || parameterName.equals("state"))) {
            redirectOnError = false;
        }

        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthenticationResult = authorizationCodeRequestAuthentication;
        if (redirectOnError && !StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())) {
            String redirectUri = resolveRedirectUri(authorizationRequest, registeredClient);
            String state = authorizationCodeRequestAuthentication.isConsent() && authorizationRequest != null ? authorizationRequest.getState() : authorizationCodeRequestAuthentication.getState();
            authorizationCodeRequestAuthenticationResult = from(authorizationCodeRequestAuthentication).redirectUri(redirectUri).state(state).build();
            authorizationCodeRequestAuthenticationResult.setAuthenticated(authorizationCodeRequestAuthentication.isAuthenticated());
        } else if (!redirectOnError && StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())) {
            authorizationCodeRequestAuthenticationResult = from(authorizationCodeRequestAuthentication).redirectUri((String) null).build();
            authorizationCodeRequestAuthenticationResult.setAuthenticated(authorizationCodeRequestAuthentication.isAuthenticated());
        }

        throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, authorizationCodeRequestAuthenticationResult);
    }

    private static String resolveRedirectUri(OAuth2AuthorizationRequest authorizationRequest, RegisteredClient registeredClient) {
        if (authorizationRequest != null && StringUtils.hasText(authorizationRequest.getRedirectUri())) {
            return authorizationRequest.getRedirectUri();
        } else {
            return registeredClient != null ? (String) registeredClient.getRedirectUris().iterator().next() : null;
        }
    }

    private static org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken.Builder from(OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication) {
        return OAuth2AuthorizationCodeRequestAuthenticationToken.with(authorizationCodeRequestAuthentication.getClientId(), (Authentication) authorizationCodeRequestAuthentication.getPrincipal()).authorizationUri(authorizationCodeRequestAuthentication.getAuthorizationUri()).redirectUri(authorizationCodeRequestAuthentication.getRedirectUri()).scopes(authorizationCodeRequestAuthentication.getScopes()).state(authorizationCodeRequestAuthentication.getState()).additionalParameters(authorizationCodeRequestAuthentication.getAdditionalParameters()).authorizationCode(authorizationCodeRequestAuthentication.getAuthorizationCode());
    }
}
