# 生成 code 的流程

## 1 调用 authorize 方法生成 code

### 1.1 AuthorizationEndpoint#authorize

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\endpoint\AuthorizationEndpoint.class
   ```

- 源码
   ```java
    // 自动授权
    @RequestMapping({"/oauth/authorize"})
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
        // ...
        
        // 生成 code
        if (responseTypes.contains("code")) {
            return new ModelAndView(this.getAuthorizationCodeResponse(authorizationRequest, (Authentication)principal));
        }
        
        // ...
    }
    
    // 人工授权
    @RequestMapping(
        value = {"/oauth/authorize"},
        method = {RequestMethod.POST},
        params = {"user_oauth_approval"}
    )
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {
        // ...
        
        // 生成 code
        var9 = this.getAuthorizationCodeResponse(authorizationRequest, (Authentication)principal);
        
        // ...
    }

    private View getAuthorizationCodeResponse(AuthorizationRequest authorizationRequest, Authentication authUser) {
        try {
            // 调用 this.generateCode 方法生成 code
            return new RedirectView(this.getSuccessfulRedirect(authorizationRequest, this.generateCode(authorizationRequest, authUser)), false, true, false);
        } catch (OAuth2Exception var4) {
            return new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, var4, false), false, true, false);
        }
    }
    
    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication) throws AuthenticationException {
        try {
            OAuth2Request storedOAuth2Request = this.getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            // 调用 this.authorizationCodeServices.createAuthorizationCode 方法生成 code
            String code = this.authorizationCodeServices.createAuthorizationCode(combinedAuth);
            return code;
        } catch (OAuth2Exception var6) {
            if (authorizationRequest.getState() != null) {
                var6.addAdditionalInformation("state", authorizationRequest.getState());
            }

            throw var6;
        }
    }
   ```

## 2 生成 code 并保存

### 1.1 RandomValueAuthorizationCodeServices#createAuthorizationCode

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\code\RandomValueAuthorizationCodeServices.class
   ```

- 源码
   ```java
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        // 生成 code
        String code = this.generator.generate();
        // 把生成的 code 保存到目标存储环境
        this.store(code, authentication);
        return code;
    }
   ```

## 3 在 code 换取 token 的过程中，先删除 code

### 3.1 AbstractTokenGranter#getAccessToken

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\token\AbstractTokenGranter.class
   ```

- 源码
   ```java
    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        // 调用 this.getOAuth2Authentication 方法消费 code
        return this.tokenServices.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
    }
   ```

### 3.2 AuthorizationCodeTokenGranter#getOAuth2Authentication

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\code\AuthorizationCodeTokenGranter.class
   ```

- 源码
   ```java
   protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        // ...
        
        // 消费 code
        OAuth2Authentication storedAuth = this.authorizationCodeServices.consumeAuthorizationCode(authorizationCode);
        
        // ...
    }
   ```

### 3.3 RandomValueAuthorizationCodeServices#consumeAuthorizationCode

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\code\RandomValueAuthorizationCodeServices.class
   ```

- 源码
   ```java
   public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
        // 删除 code
        OAuth2Authentication auth = this.remove(code);
        if (auth == null) {
            throw new InvalidGrantException("Invalid authorization code: " + code);
        } else {
            return auth;
        }
    }
   ```
