# oauth2 类

## 依赖

```xml
<properties>
    <spring-cloud.oauth2.version>2.2.5.RELEASE</spring-cloud.oauth2.version>
</properties>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-security</artifactId>
    <version>${spring-cloud.oauth2.version}</version>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>${spring-cloud.oauth2.version}</version>
</dependency>
```

## controller

- @RequestMapping({"/oauth/authorize"}): 后台自动保存 approve，并生成 code
- @RequestMapping(value = {"/oauth/authorize"}, method = {RequestMethod.POST}, params = {"user_oauth_approval"}): 手动保存 approve，并生成 code
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\endpoint\AuthorizationEndpoint.class
   ```

- @RequestMapping(value = {"/oauth/token"}, method = {RequestMethod.POST}): 生成/刷新 token
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\endpoint\TokenEndpoint.class
   ```

## 数据表相关的类

- JdbcClientDetailsService (oauth_client_details)
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\client\JdbcClientDetailsService.class
   ```
   
   ```java
   private static final String DEFAULT_SELECT_STATEMENT = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?";
   private static final String DEFAULT_INSERT_STATEMENT = "insert into oauth_client_details (client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, client_id) values (?,?,?,?,?,?,?,?,?,?,?)";
   private static final String DEFAULT_UPDATE_STATEMENT = "update oauth_client_details set " + "resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove".replaceAll(", ", "=?, ") + "=? where client_id = ?";
   private static final String DEFAULT_UPDATE_SECRET_STATEMENT = "update oauth_client_details set client_secret = ? where client_id = ?";
   private static final String DEFAULT_DELETE_STATEMENT = "delete from oauth_client_details where client_id = ?";
   ```

- JdbcApprovalStore (oauth_approvals)
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\approval\JdbcApprovalStore.class
   ```

   ```java
   private static final String DEFAULT_GET_APPROVAL_SQL = String.format("select %s from %s where userId=? and clientId=?", "expiresAt,status,lastModifiedAt,userId,clientId,scope", "oauth_approvals");
   private static final String DEFAULT_DELETE_APPROVAL_SQL = String.format("delete from %s where userId=? and clientId=? and scope=?", "oauth_approvals");
   private static final String DEFAULT_EXPIRE_APPROVAL_STATEMENT = String.format("update %s set expiresAt = ? where userId=? and clientId=? and scope=?", "oauth_approvals");
   ```

- JdbcAuthorizationCodeServices (oauth_code)
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\code\JdbcAuthorizationCodeServices.class
   ```

   ```java
   private String selectAuthenticationSql = "select code, authentication from oauth_code where code = ?";
   private String insertAuthenticationSql = "insert into oauth_code (code, authentication) values (?, ?)";
   private String deleteAuthenticationSql = "delete from oauth_code where code = ?";
   ```

- JdbcTokenStore (oauth_access_token/oauth_refresh_token)
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\token\store\JdbcTokenStore.class
   ```
   
   ```java
   private static final String DEFAULT_ACCESS_TOKEN_INSERT_STATEMENT = "insert into oauth_access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) values (?, ?, ?, ?, ?, ?, ?)";
   private static final String DEFAULT_ACCESS_TOKEN_SELECT_STATEMENT = "select token_id, token from oauth_access_token where token_id = ?";
   private static final String DEFAULT_ACCESS_TOKEN_AUTHENTICATION_SELECT_STATEMENT = "select token_id, authentication from oauth_access_token where token_id = ?";
   private static final String DEFAULT_ACCESS_TOKEN_FROM_AUTHENTICATION_SELECT_STATEMENT = "select token_id, token from oauth_access_token where authentication_id = ?";
   private static final String DEFAULT_ACCESS_TOKENS_FROM_USERNAME_AND_CLIENT_SELECT_STATEMENT = "select token_id, token from oauth_access_token where user_name = ? and client_id = ?";
   private static final String DEFAULT_ACCESS_TOKENS_FROM_USERNAME_SELECT_STATEMENT = "select token_id, token from oauth_access_token where user_name = ?";
   private static final String DEFAULT_ACCESS_TOKENS_FROM_CLIENTID_SELECT_STATEMENT = "select token_id, token from oauth_access_token where client_id = ?";
   private static final String DEFAULT_ACCESS_TOKEN_DELETE_STATEMENT = "delete from oauth_access_token where token_id = ?";
   private static final String DEFAULT_ACCESS_TOKEN_DELETE_FROM_REFRESH_TOKEN_STATEMENT = "delete from oauth_access_token where refresh_token = ?";

   private static final String DEFAULT_REFRESH_TOKEN_INSERT_STATEMENT = "insert into oauth_refresh_token (token_id, token, authentication) values (?, ?, ?)";
   private static final String DEFAULT_REFRESH_TOKEN_SELECT_STATEMENT = "select token_id, token from oauth_refresh_token where token_id = ?";
   private static final String DEFAULT_REFRESH_TOKEN_AUTHENTICATION_SELECT_STATEMENT = "select token_id, authentication from oauth_refresh_token where token_id = ?";
   private static final String DEFAULT_REFRESH_TOKEN_DELETE_STATEMENT = "delete from oauth_refresh_token where token_id = ?";
   ```
   
