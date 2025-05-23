# oauth2.1生成 token 的流程 (code 换取 token)

## 1 访问 /oauth2/authorize 接口（GET）

**在浏览器中访问授权服务器的 ```/oauth2/authorize``` 接口。**

```
http://localhost:8080/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid+profile&redirect_uri=http://localhost
```

- ```response_type=code``` : **必须**，请求的响应类型固定为 ```code```
- ```scope=openid``` :  **OIDC 请求的时候为必选项，其他请求的时候为可选项**，OIDC 请求时的 scope 参数值必须包含 ```openid```
- ```client_id=messaging-client``` : **必须**，在授权服务器上注册的 ```client_id```
- ```redirect_uri=http://localhost``` : **必须**，在授权服务器上注册的回调地址

## 2 重定向到 /oauth/login 接口（GET）

**浏览器重定向到授权服务器自定义的 loginPage （比如 ```/oauth/login```） 接口。**

**如果未认证，就重定向到授权服务器的认证接口，此时会打开 login 认证界面；否则，就直接转到步骤 6。**

```
http://localhost:8080/oauth/login
```

## 3 登录并调用 /oauth/login/authorize 接口（POST）

**输入用户名(admin)、密码(admin)，点登录之后，调用授权服务器自定义的 loginProcessingUrl （比如 ```/oauth/login/authorize```） 接口。**

```
http://localhost:8080/oauth/login/authorize
```

## 4 重定向到 /oauth2/authorize 接口（GET）

**浏览器又重定向到授权服务的 ```/oauth2/authorize``` 接口，即步骤 1 所访问的授权接口地址。**

```
http://localhost:8080/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid+profile&redirect_uri=http://localhost
```

## 5 授权并调用 /oauth2/authorize 接口（POST）

**如果不满足自动授权的条件，就执行本步骤 5; 否则，就直接转到步骤  6 （后台自动授权）。**

**点授权之后，调用授权服务的 /oauth2/authorize 接口。**

```
http://localhost:8080/oauth2/authorize
```

## 6 重定向并返回 code（GET）

**浏览器重定向并返回 code。**

```
http://localhost/?code=QHu3OxrHtN64DP0fu3Nc1OkKBaV8A6tjUQ4v9Nk64mMsFWVnLWlZr2Wnr4DRTtbho64Tr814csVZTIRjbY3vbmxoKLfMR7sZIyQ6R_sp0Dt3YE4QD4NuvMr1GFE4sEQ0
```

- ```code=QHu3OxrHtN64DP0fu3Nc1OkKBaV8A6tjUQ4v9Nk64mMsFWVnLWlZr2Wnr4DRTtbho64Tr814csVZTIRjbY3vbmxoKLfMR7sZIyQ6R_sp0Dt3YE4QD4NuvMr1GFE4sEQ0``` : code 就是授权码

## 7 获取 token 调用 /oauth2/token 接口（POST）

**调用授权服务的 ```/oauth2/token``` 接口。注意: ```grant_type=authorization_code```。**

1. **postman:**
   ```bash
   http://localhost:8080/oauth2/token
   ```

   body 的 ```x-www-form-urlencoded``` 参数:
     - ```grant_type=authorization_code``` : **必须**，授权类型固定为 ```authorization_code```
     - ```client_id=messaging-client``` : **必须**，在授权服务器上注册的 ```client_id```
     - ```client_secret=``` : **必须**，在授权服务器上注册的 ```client_secret```
     - ```redirect_uri=http://localhost``` : **必须**，在授权服务器上注册的回调地址
     - ```code=<CODE>``` : **必须**，授权服务器返回的授权码

2. **curl:**
   ```bash
   curl --location --request POST 'http://localhost:8080/oauth2/token' \
   --header 'Content-Type: application/x-www-form-urlencoded' \
   --data-urlencode 'grant_type=authorization_code' \
   --data-urlencode 'code=<CODE>' \
   --data-urlencode 'client_id=messaging-client' \
   --data-urlencode 'client_secret=secret' \
   --data-urlencode 'redirect_uri=http://localhost'
   ```

成功获取 token 如下: 

```json
{
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVpZCI6IjIiLCJjcmVhdGVkIjoxNzQ3OTA0NzA1NDYzLCJ1dHlwZSI6IjIiLCJpZCI6ImQwMjU5MzYyYzM1ODRlMjBiYjcxNzJhZDgwNmNiMTU0Iiwib2F1dGhUeXBlIjoiMSIsInRpZCI6ImI4ZTgzYTUxOWE0MTQxODU4M2JkODQ3ZThiMTA4ZjIyIiwicGxhdGZvcm0iOiJvYXV0aDIiLCJzaWQiOiI5ZGUxMTVmN2EwYmM0NGFjOGQ4ZGMwMGI1OTkyOGUyZiIsImV4cCI6MTc0NzkxMTkwNX0.j8Xg6baKS4PoYGLT8-gjxVrGxfmWXxo-h13dC9i2tXXSx7v8fbd0-53zGf719xZRVhGsd5wxlq1_ZHkV8g9niw",
    "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVpZCI6IjIiLCJjcmVhdGVkIjoxNzQ3OTA0NzA2Njk0LCJ1dHlwZSI6IjIiLCJpZCI6Ijk4NTY1NDgzNDE0NTQ3NDFhZTUzNjJhNzY2OGI5ZWFhIiwib2F1dGhUeXBlIjoiMSIsInRpZCI6ImI4ZTgzYTUxOWE0MTQxODU4M2JkODQ3ZThiMTA4ZjIyIiwicGxhdGZvcm0iOiJvYXV0aDIiLCJzaWQiOiI1MTcyYzRhOTc1MTk0YzE4YWZmMmJkZjQxZGM5MmRjYSIsImV4cCI6MTc0NzkxMTkwNn0.Xq3HrGS_pT_OSWHiryxTKi2X9RETNYiW-G70pILVDy68r4DnKMwGNcEdx5pt5Ux6ioQmXAZlg9O2Fywtv6kZsQ",
    "scope": "openid profile",
    "id_token": "eyJraWQiOiJiOWVmYWZhMC0yNzM2LTQ1YTItODM4ZS1iNjY5YmVhYWQwOGEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im9pZGMtY2xpZW50IiwiYXpwIjoib2lkYy1jbGllbnQiLCJhdXRoX3RpbWUiOjE3NDc5MDQ2ODYsImlzcyI6Imh0dHA6Ly8yMC4wLjMuMTA1Ojg4ODgiLCJuYW1lIjoiYWRtaW4iLCJleHAiOjE3NDc5MDY1MDYsImlhdCI6MTc0NzkwNDcwNiwianRpIjoiNGVlOWYzNmEtZmM1ZC00NGQ3LThmYWQtMzA4NmQzMDgzMzUwIiwiZW1haWwiOiJ4dXl1YW56aGlAc2FmdG9wLmNuIiwic2lkIjoieVlLVVRVRFY4Y1NkQlpHU2RBUW9rWlpzdEphM2hQYXdXVlIwSktEb0ZPayJ9.Ju3o_3-G5yr5w4J5OE3KQj_oC1QCgao_BQL1SQdAN7uI9NVXJadrg_FTmCcWegrPkapJVzdVRUK3tMRv9ZzXtwWqMFvSEbEif4X05R9p0Burmo1n7Lp_ddsWgT3B5mplrdh6DwFTWiBwAfXYYAma9Fr3kQkXJSvGNdwLaP_wrbZ-3SdCw0veI5SLyRse3k1KXMOlM6t_GrZOYP_bWJY7h4lbDMQjXtLXCR9KC5nuwJVxb-95J-_a0VazQgUyqX46nC9X-bolMOt4gd9y2hqH6glt4gF4pcbX7eWiX1ok45Sn0SjjYD-QF11gBPZ0TryJCpLP_m9Z6kRTPxNW63c3iQ",
    "token_type": "Bearer",
    "expires_in": 7200
}
```

## 8 获取 userinfo 调用 /userinfo 接口（GET）

**调用授权服务器的 ```/userinfo``` 接口。**

1. **postman:**
   ```bash
   http://localhost:8080/userinfo
   ```
   
   1. Authorization 的 Type 选择 ```Bearer Token```
   2. 在 Token 栏里填写授权服务器返回的 ```ACCESS_TOKEN```

2. **curl:**
   ```bash
   curl -X GET -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/userinfo
   ```

成功获取 userinfo 如下：

```json
{
    "sub": "admin",
    "aud": [
        "messaging-client"
    ],
    "azp": "messaging-client",
    "auth_time": 1747904686364,
    "iss": "localhost:8080",
    "name": "admin",
    "exp": 1747906559.364410200,
    "iat": 1747904759.364410200,
    "jti": "6221056c-15c5-4c85-98bb-3059e3f5d08a",
    "email": "xxx@163.com",
    "sid": "yYKUTUDV8cSdBZGSdAQokZZstJa3hPawWVR0JKDoFOk"
}
```

## 9 刷新 token 调用 /oauth2/token 接口（POST）

**调用授权服务器的 ```/oauth2/token``` 接口。注意: ```grant_type=refresh_token```。**

1. **postman:**
   ```bash
   http://localhost:8080/oauth2/token
   ```

   body 的 ```x-www-form-urlencoded``` 参数:
     - ```grant_type=refresh_token``` : **必须**，授权类型固定为 ```refresh_token```
     - ```client_id=messaging-client``` : **必须**，在授权服务器上注册的 ```client_id```
     - ```client_secret=secret``` : **必须**，在授权服务器上注册的 ```client_secret```
     - ```refresh_token=<REFRESH_TOKEN>``` : **必须**，授权服务器返回的 ```refresh_token```

2. **curl:**
   ```bash
   curl --location --request POST 'http://localhost:8080/oauth2/token' \
   --header 'Content-Type: application/x-www-form-urlencoded' \
   --data-urlencode 'grant_type=refresh_token' \
   --data-urlencode 'refresh_token=<REFRESH_TOKEN>' \
   --data-urlencode 'client_id=messaging-client' \
   --data-urlencode 'client_secret=secret'
   ```

刷新 token 如下：

```json
{
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVpZCI6IjIiLCJjcmVhdGVkIjoxNzQ3OTYyODgwMzY3LCJ1dHlwZSI6IjIiLCJpZCI6ImE2YWFlOWMwNDNiZDQ0YTFiMTlkMDdkMjk1M2E1YWQ1Iiwib2F1dGhUeXBlIjoiMSIsInRpZCI6ImI4ZTgzYTUxOWE0MTQxODU4M2JkODQ3ZThiMTA4ZjIyIiwicGxhdGZvcm0iOiJvYXV0aDIiLCJzaWQiOiI1YTY3YWFjNmRhODc0MGEyYjI4MmFjMDM0OTI1YjJmYyIsImV4cCI6MTc0Nzk3MDA4MH0.hnasFUl7VFMwLckCF1b5cKh-5AdQ3iSWKVaTtRN8OaeSKuqV0kimN-zhRt1y5tsNS4fgSFf5z6m4ewc453_fhw",
    "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVpZCI6IjIiLCJjcmVhdGVkIjoxNzQ3OTYyODcyMDkxLCJ1dHlwZSI6IjIiLCJpZCI6Ijk1YTk1Y2ZjZWFjNzRmNDc4NjdmZGQ1M2QxMzQwZTkwIiwib2F1dGhUeXBlIjoiMSIsInRpZCI6ImI4ZTgzYTUxOWE0MTQxODU4M2JkODQ3ZThiMTA4ZjIyIiwicGxhdGZvcm0iOiJvYXV0aDIiLCJzaWQiOiI4MWI4NThiNWRiODY0MzE2OTM2MDQ1ZWVmYTM3NmJjOSIsImV4cCI6MTc0Nzk3MDA3Mn0.Nu3dSDW-czW0rSBkLnORwjO4y7UWE4_qTd2FPngjrepIAtVgv8m_8o0o2wW7CCO3M4hds6jXmEBSNj0vxgOrAg",
    "scope": "openid profile",
    "id_token": "eyJraWQiOiJiOWVmYWZhMC0yNzM2LTQ1YTItODM4ZS1iNjY5YmVhYWQwOGEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im9pZGMtY2xpZW50IiwiYXpwIjoib2lkYy1jbGllbnQiLCJhdXRoX3RpbWUiOjE3NDc5NjI4NjIsImlzcyI6Imh0dHA6Ly8yMC4wLjMuMTA1Ojg4ODgiLCJuYW1lIjoiYWRtaW4iLCJleHAiOjE3NDc5NjQ2ODAsImlhdCI6MTc0Nzk2Mjg4MCwianRpIjoiNTMzNmQxYzItYjU3MC00ZThmLWEyMzYtODNkNjkzNjFlOWFkIiwiZW1haWwiOiJ4dXl1YW56aGlAc2FmdG9wLmNuIiwic2lkIjoiMnNVNWtxNW9qcmN3N1c3cUk3X1hVYk50ejZuZTNIVDljRDdvc3ZSVVBSNCJ9.AmMjuMkxnXlUHf17eTrWiFG6YfIKvP7k1JPYfFL1fKP8gVU8sfGUf1JWCom6ntA_rIQbKaopVhLhwkbS7rnyiexA2x3lyIRYzUgImswF2HSFB6RNGnjOiVCnYFT95Ezb-E7dLmMJE04UUrV4G20DQCqNco6Ue52nNf6Uvhijsuy6hM6lC3txjPIvUVNfJgDlTBEXGqStZlegxTeBwj2ERJxG-OruX3Devzd_1Y5KJRMIE_gBm1TMkj6QK9BJ7Cl_ict3UjDUbOdVmCTFg-hi3-m5PrhkXJqTCVtVquypoDScCGizxZpIpsswedcMRKYeF-2vzo6gzWfWBYxAcAoN6g",
    "token_type": "Bearer",
    "expires_in": 7200
}
```

## 10 查看授权服务配置

```bash
curl --location --request GET 'http://localhost:8080/.well-known/openid-configuration'
```

响应：

```json
{
    "issuer": "http://localhost:8080",
    "authorization_endpoint": "http://localhost:8080/oauth2/authorize",
    "device_authorization_endpoint": "http://localhost:8080/oauth2/device_authorization",
    "token_endpoint": "http://localhost:8080/oauth2/token",
    "token_endpoint_auth_methods_supported": [
        "client_secret_basic",
        "client_secret_post",
        "client_secret_jwt",
        "private_key_jwt",
        "tls_client_auth",
        "self_signed_tls_client_auth"
    ],
    "jwks_uri": "http://localhost:8080/oauth2/jwks",
    "userinfo_endpoint": "http://localhost:8080/userinfo",
    "end_session_endpoint": "http://localhost:8080/connect/logout",
    "response_types_supported": [
        "code"
    ],
    "grant_types_supported": [
        "authorization_code",
        "client_credentials",
        "refresh_token",
        "urn:ietf:params:oauth:grant-type:device_code",
        "urn:ietf:params:oauth:grant-type:token-exchange"
    ],
    "revocation_endpoint": "http://localhost:8080/oauth2/revoke",
    "revocation_endpoint_auth_methods_supported": [
        "client_secret_basic",
        "client_secret_post",
        "client_secret_jwt",
        "private_key_jwt",
        "tls_client_auth",
        "self_signed_tls_client_auth"
    ],
    "introspection_endpoint": "http://localhost:8080/oauth2/introspect",
    "introspection_endpoint_auth_methods_supported": [
        "client_secret_basic",
        "client_secret_post",
        "client_secret_jwt",
        "private_key_jwt",
        "tls_client_auth",
        "self_signed_tls_client_auth"
    ],
    "code_challenge_methods_supported": [
        "S256"
    ],
    "tls_client_certificate_bound_access_tokens": true,
    "subject_types_supported": [
        "public"
    ],
    "id_token_signing_alg_values_supported": [
        "RS256"
    ],
    "scopes_supported": [
        "openid"
    ]
}
```

## 11 oauth2 源码

***以 ```spring-boot-starter-oauth2-authorization-server:1.4.2``` 源码为例。***

filter 入口源码:

- ```OAuth2ClientAuthenticationFilter```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\web\OAuth2ClientAuthenticationFilter.class```

filter 源码:

- ```/oauth2/authorize```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\web\OAuth2AuthorizationEndpointFilter.class```
- ```/oauth2/token```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\web\OAuth2TokenEndpointFilter.class```
- ```/userinfo```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\oidc\web\OidcUserInfoEndpointFilter.class```

generator 源码:

- ```AuthorizationCodeGenerator```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\authentication\OAuth2AuthorizationCodeGenerator.class```
- ```AccessTokenGenerator```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\token\OAuth2AccessTokenGenerator.class```
- ```RefreshTokenGenerator```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\token\OAuth2RefreshTokenGenerator.class```
