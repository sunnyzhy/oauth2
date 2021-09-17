# 直接访问访问 resource 的流程

## 流程分析

### 1 访问资源服务的 /message 接口（GET）

```
http://localhost:8092/message
```

### 2 重定向到授权服务的 /oauth/authorize 接口（GET）

```
http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
```

### 3 重定向到授权服务的 /login 接口（GET）

```
http://localhost:8090/login
```

### 4 登录并调用授权服务的 /login 接口（POST）

**输入用户名(admin)、密码(admin)，点登录之后，调用授权服务器的 /login 接口。**

```
http://localhost:8090/login
```

### 5 重定向到授权服务的 /oauth/authorize 接口（GET）

**浏览器又重定向到授权服务的 /oauth/authorize 接口，即步骤 2 所访问的授权接口地址。**

```
http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
```

### 6 授权并调用授权服务的 /oauth/authorize 接口（POST）

**如果不满足自动授权的条件，就执行本步骤 6; 否则，就直接转到步骤  7 （后台自动授权）。**

**点授权之后，调用授权服务的 /oauth/authorize 接口。**

```
http://localhost:8090/oauth/authorize
```

### 7 重定向并返回 code（GET）

**浏览器重定向并返回 code。**

```
http://localhost?code=W636oE
```

### 8 用 code 通过授权服务换取 access_token

```json
   {
        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IllLeDFWTDlNL0RDT3ZOWCt0OWxUQXJpUXJtVlhtSDl6b3FQQWFnMDh4U2s9In0.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsiMzAiXSwiaWQiOiIyMGRjOTNjNGYzN2Y0NzVhODExZTUyMTMwYjBlYjBiOCIsImV4cCI6MTYzMTcxMzM4MywiYXV0aG9yaXRpZXMiOlsiVVNFUiIsIkFETUlOIl0sImp0aSI6ImQyNzYyNDkzLWFjNGYtNDQyMi1iMDM0LWNiZjU1Y2FhYmNmZiIsImNsaWVudF9pZCI6Im1lc3NhZ2luZy1jbGllbnQifQ.C7SRAxnp9I5_5YhDrrqRSzvtCsB__k3erFS1BQN8E2QlM4zQpGLfYtNtUPjhlcc2fZ1yo4YGXhxdcAObspXgXPuQkZIya6_kkyD0WzrI8WFr3GzjWBxHHVs9Go9zM39RCwSOE2eVUiqVz8zFHdmZAI0rlBhwVBFYVWlmwJ4wPqltHpGj2ZYzTmThj3Mj8E34K1DhKk2VzQVr1RmB5V08nQqQEKLFQOAZDG6t7ehpkConAW4m6nJ-EkczZUX8fhu5WA8yJftSYP7KEjGl8m0Zkb4M8Afjpw8N5LrrH4aZC0u2MkucsDN3E6rAk8R8QwJUIw-n79SH5bYyg4-MeBduEw",
        "token_type": "bearer",
        "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IllLeDFWTDlNL0RDT3ZOWCt0OWxUQXJpUXJtVlhtSDl6b3FQQWFnMDh4U2s9In0.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsiMzAiXSwiYXRpIjoiZDI3NjI0OTMtYWM0Zi00NDIyLWIwMzQtY2JmNTVjYWFiY2ZmIiwiaWQiOiIyMGRjOTNjNGYzN2Y0NzVhODExZTUyMTMwYjBlYjBiOCIsImV4cCI6MTYzMTcxMzM4MywiYXV0aG9yaXRpZXMiOlsiVVNFUiIsIkFETUlOIl0sImp0aSI6Ijc2M2QwNjY1LTlkZTUtNDdiMS05OGQ3LTM0NWU4ZTIxMjRkZCIsImNsaWVudF9pZCI6Im1lc3NhZ2luZy1jbGllbnQifQ.YjBie37Np4_WR_O84u8ulHgiBsnFmUe9ImWnzNIpv6f06jmQ93kY8f1xInLIaYEvRPXrRO_X9-WjpEz1eGi9p0Ve6zn8CFiJxA9DqOiydmhwwNbMNHzW4BSVCZcxGT1bSFayPsYJO1wzfwFLZoryRKb_AwZpQER7K8GdU6vjZB5MBQzk75xfLgjOx8BjxRJwtu5Bqx6f49NT7zlO_ceSgCPiHUez18qxzLXzH8VbErcSddYNsECSqQdfdkbk-kPUksllysqz0TQvOEtKepbxDxwHtMBretbcn7JWhPgtZxEZeb48VVFxxRxFYod_R5TUyL6v0WhUcCJOudCNtsHqQQ",
        "expires_in": 24460,
        "scope": "30",
        "sub": "admin",
        "id": "20dc93c4f37f475a811e52130b0eb0b8",
        "jti": "d2762493-ac4f-4422-b034-cbf55caabcff"
   }
```

### 9 访问资源服务的 /message 接口（GET）

```
GET http://localhost:8092/message?access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IllLeDFWTDlNL0RDT3ZOWCt0OWxUQXJpUXJtVlhtSDl6b3FQQWFnMDh4U2s9In0.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsiMzAiXSwiaWQiOiIyMGRjOTNjNGYzN2Y0NzVhODExZTUyMTMwYjBlYjBiOCIsImV4cCI6MTYzMTcxMzM4MywiYXV0aG9yaXRpZXMiOlsiVVNFUiIsIkFETUlOIl0sImp0aSI6ImQyNzYyNDkzLWFjNGYtNDQyMi1iMDM0LWNiZjU1Y2FhYmNmZiIsImNsaWVudF9pZCI6Im1lc3NhZ2luZy1jbGllbnQifQ.C7SRAxnp9I5_5YhDrrqRSzvtCsB__k3erFS1BQN8E2QlM4zQpGLfYtNtUPjhlcc2fZ1yo4YGXhxdcAObspXgXPuQkZIya6_kkyD0WzrI8WFr3GzjWBxHHVs9Go9zM39RCwSOE2eVUiqVz8zFHdmZAI0rlBhwVBFYVWlmwJ4wPqltHpGj2ZYzTmThj3Mj8E34K1DhKk2VzQVr1RmB5V08nQqQEKLFQOAZDG6t7ehpkConAW4m6nJ-EkczZUX8fhu5WA8yJftSYP7KEjGl8m0Zkb4M8Afjpw8N5LrrH4aZC0u2MkucsDN3E6rAk8R8QwJUIw-n79SH5bYyg4-MeBduEw

message method is ok
```

## 源码分析

### 1 FilterChainProxy#doFilter

- 源文件
   ```
   \org\springframework\security\spring-security-web\5.5.0\spring-security-web-5.5.0.jar!\org\springframework\security\web\FilterChainProxy.class
   ```

- 源码
   ```java
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // ...
        
        // 1. 进入 org.springframework.security.web 的过滤链
        this.doFilterInternal(request, response, chain);
        
        // ...
    }
   ```

### 2 OAuth2AuthenticationManager#authenticate

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\authentication\OAuth2AuthenticationManager.class
   ```

- 源码
   ```java
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // ...
        
        // 2.1 从上下文里提取 access_token
        String token = (String)authentication.getPrincipal();
        
        // 2.3 验证 access_token
        OAuth2Authentication auth = this.tokenServices.loadAuthentication(token);

        // ...
    }
   ```

### 3 DefaultTokenServices#loadAuthentication

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\token\DefaultTokenServices.class
   ```

- 源码
   ```java
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        // 3. 从 token 的存储源里读取 access_token
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(accessTokenValue);

        // ...
    }
   ```

### 4 JdbcTokenStore#readAccessToken

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\token\store\JdbcTokenStore.class
   ```

- 源码
   ```java
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken = null;

        try {
            // 4.1 把 access_token 转换为 token_id
            // 4.2 从 oauth_access_token 表里查询 token_id 对应的数据
            // 4.3 如果没有找到 token_id 对应的数据，就说明 access_token 验证失败，拒绝访问资源地址；否则，就说明 access_token 验证成功，允许访问资源地址
            accessToken = (OAuth2AccessToken)this.jdbcTemplate.queryForObject(this.selectAccessTokenSql, new RowMapper<OAuth2AccessToken>() {
                public OAuth2AccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return JdbcTokenStore.this.deserializeAccessToken(rs.getBytes(2));
                }
            }, new Object[]{this.extractTokenKey(tokenValue)});
        } catch (EmptyResultDataAccessException var4) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + tokenValue);
            }
        } catch (IllegalArgumentException var5) {
            LOG.warn("Failed to deserialize access token for " + tokenValue, var5);
            this.removeAccessToken(tokenValue);
        }

        return accessToken;
    }
   ```
   
