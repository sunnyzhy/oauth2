# 访问 resource 的流程

## 1 FilterChainProxy#doFilter

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

## 2 OAuth2AuthenticationManager#authenticate

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

## 3 DefaultTokenServices#loadAuthentication

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

## 4 JdbcTokenStore#readAccessToken

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
   
