# Spring Authorization Server

## Endpoint

- ```/oauth2/authorize```:
   - uri: ```.\org\springframework\security\spring-security-oauth2-authorization-server\0.3.1\spring-security-oauth2-authorization-server-0.3.1.jar!\org\springframework\security\oauth2\server\authorization\web\OAuth2AuthorizationEndpointFilter.class```

- ```/oauth2/token```:
   - uri: ```.\org\springframework\security\spring-security-oauth2-authorization-server\0.3.1\spring-security-oauth2-authorization-server-0.3.1.jar!\org\springframework\security\oauth2\server\authorization\web\OAuth2TokenEndpointFilter.class```

- ```/userinfo```:
   - uri: ```.\org\springframework\security\spring-security-oauth2-authorization-server\0.3.1\spring-security-oauth2-authorization-server-0.3.1.jar!\org\springframework\security\oauth2\server\authorization\oidc\authentication\OidcUserInfoAuthenticationProvider.class```

   - userinfo: ```.\org\springframework\security\spring-security-oauth2-authorization-server\0.3.1\spring-security-oauth2-authorization-server-0.3.1.jar!\org\springframework\security\oauth2\server\authorization\oidc\authentication\OidcUserInfoAuthenticationProvider.class```

## 创建数据源

1. 下载 [oauth2.sql](./src/main/db/oauth2.sql 'oauth2.sql')

2. 导入数据表

   ```sql
   source oauth2.sql;
   ```

## 添加依赖

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-authorization-server</artifactId>
    <version>0.3.1</version>
</dependency>
```

## 启动应用

启动 ```SpringBootOauth2AuthorizationServerApplication```

## 新增 registered_client

```bash
curl -X POST  -H "Content-Type: application/json" -d '{"clientId":"messaging-client","clientSecret":"secret","clientName":"messaging-client","redirectUri":"https://www.baidu.com","scopes":["read","write"]}' "http://localhost:8080/client"
```

## 获取 token

1. 获取 code: 在浏览器的地址栏输入 ```http://localhost:8080/oauth2/authorize?response_type=code&scope=openid+profile+email+address+phone&client_id=messaging-client&redirect_uri=https://www.baidu.com```, ```用户名/密码:admin/admin```
   示例:
      ```code=jyRCLuFs4ytRgqCwu_OO-aRCazfdVGUzrkH7WUILPasPfCi2Uk_bv1EfLH86GlrI7ByBbpP9FzMua9wXxykEQ9A7OSFIJdmJ2Kcy7Q59BjCcp-E7HQRL8LMwnQd_1x6x```

2. 获取 token:
   ```bash
   curl -X POST -d "grant_type=authorization_code&code=<CODE>&client_id=messaging-client&client_secret=secret&redirect_uri=https://www.baidu.com" http://localhost:8080/oauth2/token
   ```
   示例:
      ```bash
      # curl -X POST -d "grant_type=authorization_code&code=jyRCLuFs4ytRgqCwu_OO-aRCazfdVGUzrkH7WUILPasPfCi2Uk_bv1EfLH86GlrI7ByBbpP9FzMua9wXxykEQ9A7OSFIJdmJ2Kcy7Q59BjCcp-E7HQRL8LMwnQd_1x6x&client_id=messaging-client&client_secret=secret&redirect_uri=https://www.baidu.com" http://localhost:8080/oauth2/token | jq
      {
          "access_token": "eyJraWQiOiI4MzAzMDMzNC1mNTJmLTQ2YjYtOThkOS01NWMzMjhiOTM5ZDYiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE2NjU5NzA4NjEsInBob25lIjoiMTIzNDU2Iiwic2NvcGUiOlsiYWRkcmVzcyIsInBob25lIiwib3BlbmlkIiwicHJvZmlsZSIsImVtYWlsIl0sImlzcyI6Imh0dHA6XC9cLzIwLjAuMC40ODo4MDgwIiwiaWQiOiJkZTUyZGY0M2I2Y2U0YWU3YWI2NWZjZGI1MTMxMDE1YSIsImV4cCI6MTY2NTk3NDQ2MSwiaWF0IjoxNjY1OTcwODYxLCJ1c2VySWQiOiIxIiwiZW1haWwiOiIxMjNAMTIzLmNvbSJ9.pQpuJW_nWA-qrjGkVWmT3yhJ_2Hhj89RIrfoPPGNxAIQyj1lTKRNxObROA6JOatu6pIQ8uzb7uh-dI64QWbOjQ",
          "refresh_token": "bdv_p_yZ1BryT3dMhPC0h2OSZBM7CvBiRz_gNFLQIGpEQnVkgZyo9BmO9uKFVGtGUun8fW1zH3zCv9xeA8C65CADKvoDF8v7LuQmxxUuU5INv5Agrxh7RkoOusDMQhpv",
          "scope": "address phone openid profile email",
          "id_token": "eyJraWQiOiI4MzAzMDMzNC1mNTJmLTQ2YjYtOThkOS01NWMzMjhiOTM5ZDYiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJhZGRyZXNzIjoiQ04iLCJhenAiOiJtZXNzYWdpbmctY2xpZW50IiwicHJvZmlsZSI6ImNvbS5mYXN0ZXJ4bWwuamFja3Nvbi5kYXRhYmluZC5PYmplY3RNYXBwZXJANWI4OTc1ODEiLCJpc3MiOiJodHRwOlwvXC8yMC4wLjAuNDg6ODA4MCIsInBob25lX251bWJlciI6IjEyMzQ1NiIsImV4cCI6MTY2NTk3MjY2MiwiaWF0IjoxNjY1OTcwODYyLCJ1c2VySWQiOiIxIiwiZW1haWwiOiIxMjNAMTIzLmNvbSJ9.7qYzrqZMTq3jEs8dKiNWw_Z3zb9sTsidjsRInanL2sGQETqsmEKDibUHgNJ1sPwmGrqEexth4ap6-E2xhIngVg",
          "token_type": "Bearer",
          "expires_in": 3600
      }
      ```

3. 调用 userinfo:

   ```bash
   curl -X GET -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/userinfo
   ```

   示例:
      ```bash
      # curl -X GET -H "Authorization: Bearer eyJraWQiOiI4MzAzMDMzNC1mNTJmLTQ2YjYtOThkOS01NWMzMjhiOTM5ZDYiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE2NjU5NzA4NjEsInBob25lIjoiMTIzNDU2Iiwic2NvcGUiOlsiYWRkcmVzcyIsInBob25lIiwib3BlbmlkIiwicHJvZmlsZSIsImVtYWlsIl0sImlzcyI6Imh0dHA6XC9cLzIwLjAuMC40ODo4MDgwIiwiaWQiOiJkZTUyZGY0M2I2Y2U0YWU3YWI2NWZjZGI1MTMxMDE1YSIsImV4cCI6MTY2NTk3NDQ2MSwiaWF0IjoxNjY1OTcwODYxLCJ1c2VySWQiOiIxIiwiZW1haWwiOiIxMjNAMTIzLmNvbSJ9.pQpuJW_nWA-qrjGkVWmT3yhJ_2Hhj89RIrfoPPGNxAIQyj1lTKRNxObROA6JOatu6pIQ8uzb7uh-dI64QWbOjQ" http://localhost:8080/userinfo | jq
      {
        "sub": "admin",
        "address": "CN",
        "profile": "{\"id\":1,\"age\":20}",
        "phone_number": "123456",
        "email": "123@123.com"
      }
      ```

4. 刷新 token:

   ```bash
   curl -X POST -d "grant_type=refresh_token&refresh_token=<REFRESH_TOKEN>&client_id=messaging-client&client_secret=secret" http://localhost:8080/oauth2/token
   ```
   示例:
      ```bash
      # curl -X POST -d "grant_type=refresh_token&refresh_token=bdv_p_yZ1BryT3dMhPC0h2OSZBM7CvBiRz_gNFLQIGpEQnVkgZyo9BmO9uKFVGtGUun8fW1zH3zCv9xeA8C65CADKvoDF8v7LuQmxxUuU5INv5Agrxh7RkoOusDMQhpv&client_id=messaging-client&client_secret=secret" http://localhost:8080/oauth2/token | jq
      {
          "access_token": "eyJraWQiOiI4MzAzMDMzNC1mNTJmLTQ2YjYtOThkOS01NWMzMjhiOTM5ZDYiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE2NjU5NzI1NjIsInBob25lIjoiMTIzNDU2Iiwic2NvcGUiOlsiYWRkcmVzcyIsInBob25lIiwib3BlbmlkIiwicHJvZmlsZSIsImVtYWlsIl0sImlzcyI6Imh0dHA6XC9cLzIwLjAuMC40ODo4MDgwIiwiaWQiOiIxZTJhYjAxYWJmYTA0Nzg3OTNiNjgzMjQwY2NhZTBhNiIsImV4cCI6MTY2NTk3NjE2MiwiaWF0IjoxNjY1OTcyNTYyLCJ1c2VySWQiOiIxIiwiZW1haWwiOiIxMjNAMTIzLmNvbSJ9.ivd1jNxicgnblzzsENHRuQah1lSGcbPZtZGWrgul3yzNIunevsGrd5t3UPPyWrRcHPDVGuGOJzMkG1zGKU43qQ",
          "refresh_token": "bdv_p_yZ1BryT3dMhPC0h2OSZBM7CvBiRz_gNFLQIGpEQnVkgZyo9BmO9uKFVGtGUun8fW1zH3zCv9xeA8C65CADKvoDF8v7LuQmxxUuU5INv5Agrxh7RkoOusDMQhpv",
          "scope": "address phone openid profile email",
          "id_token": "eyJraWQiOiI4MzAzMDMzNC1mNTJmLTQ2YjYtOThkOS01NWMzMjhiOTM5ZDYiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJhZGRyZXNzIjoiQ04iLCJhenAiOiJtZXNzYWdpbmctY2xpZW50IiwicHJvZmlsZSI6ImNvbS5mYXN0ZXJ4bWwuamFja3Nvbi5kYXRhYmluZC5PYmplY3RNYXBwZXJANWI4OTc1ODEiLCJpc3MiOiJodHRwOlwvXC8yMC4wLjAuNDg6ODA4MCIsInBob25lX251bWJlciI6IjEyMzQ1NiIsImV4cCI6MTY2NTk3NDM2MiwiaWF0IjoxNjY1OTcyNTYyLCJ1c2VySWQiOiIxIiwiZW1haWwiOiIxMjNAMTIzLmNvbSJ9.gX83CvvwLgScbWBMHytm7Oh5Iwzu1BGpkmrv6RV72MkSZWTx7KvYwZ322fuBxkdT_UQA1EGGCGVR71KeuDRZTA",
          "token_type": "Bearer",
          "expires_in": 3600
      }
      ```
