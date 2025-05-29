# Spring Authorization Server

## EndpointFilter

- ```/oauth2/authorize```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\web\OAuth2AuthorizationEndpointFilter.class```

- ```/oauth2/token```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\web\OAuth2TokenEndpointFilter.class```

- ```/userinfo```: ```org\springframework\security\spring-security-oauth2-authorization-server\1.4.2\spring-security-oauth2-authorization-server-1.4.2.jar!\org\springframework\security\oauth2\server\authorization\oidc\web\OidcUserInfoEndpointFilter.class```

## 创建数据源

1. 下载 [oauth2.sql](./src/main/db/oauth2.sql 'oauth2.sql')

2. 导入数据表

   ```sql
   source oauth2.sql;
   ```

## 添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-authorization-server</artifactId>
</dependency>
```

## 启动应用

启动 ```SpringBootOauth2AuthorizationServerApplication```

## 新增 registered_client

- 方法1: 单元测试 ```SpringBootOauth2AuthorizationServerApplicationTests#save()```
- 方法2: 使用curl
   
  ```bash
   curl --location --request POST 'http://localhost:8080/api/client' \
   --header 'Content-Type: application/json' \
   --data-raw '{
       "clientId": "oidc-client",
       "clientSecret": "admin",
       "clientName": "oidc-client",
       "redirectUri": "https://www.baidu.com"
   }'
   ```

## 获取 token

1. 获取 code: 在浏览器的地址栏输入 ```http://localhost:8080/oauth2/authorize?client_id=oidc-client&response_type=code&scope=openid+profile&redirect_uri=https://www.baidu.com```

   - ```用户名/密码```: ```admin/admin```
   - ```返回的 code=8t42KhuM```

2. 获取 token:

      ```bash
      # curl --location --request POST 'http://localhost:8080/oauth2/token' \
      --header 'Content-Type: application/x-www-form-urlencoded' \
      --data-urlencode 'grant_type=authorization_code' \
      --data-urlencode 'code=8t42KhuM' \
      --data-urlencode 'client_id=oidc-client' \
      --data-urlencode 'client_secret=admin' \
      --data-urlencode 'redirect_uri=https://www.baidu.com'

      {
          "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJ0ZW5hbnRfaWQiOiIzMDFmMDNhZThmOTk0OTJkOGNmZDEyZDJlNmFjMTA3ZCIsInN1YiI6ImFkbWluIiwiY3JlYXRlZCI6MTc0ODQ5OTc2OTY0OCwic2Vzc2lvbl9pZCI6ImQ1YmU3NGY4NGU5MjQ0M2Y5MmMzNmJmYzE5OTgxYzZiIiwiaWQiOiI5MzMzZDZiZTk5Mzg0YTM5YjM0NWJlOTE0YjQzM2Q1YSIsImV4cCI6MTc0ODUwNjk2OX0.VfcxwTRZRmfjfDaTT5j3u8ND7hgD4xcInwJs68z4uWAn8CYUXrqncnFmIl-8FquyhJ1Ur9msc1YZnPMPSM_wKg",
          "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJ0ZW5hbnRfaWQiOiIzMDFmMDNhZThmOTk0OTJkOGNmZDEyZDJlNmFjMTA3ZCIsInN1YiI6ImFkbWluIiwiY3JlYXRlZCI6MTc0ODQ5OTc2OTc3MSwic2Vzc2lvbl9pZCI6IjVkNWRiYjZlMWJhNDQ0MTk5YzEzNDBlOTNkMWRlYmIxIiwiaWQiOiI1NDAwZTM2MDlkNGY0MzYzYjBiZDljZjQ3NTAwYzZmNCIsImV4cCI6MTc0ODUwNjk2OX0.b7P170BM91iBXkkWB6JsJHCgu5_FzxpWzaC-ym6b-D5Gnmw3uZPBtr0DDDa1DZSHh06mP20256fITgZfb_Tr1w",
          "scope": "openid profile",
          "id_token": "eyJraWQiOiI2MWVlNjcwNy1jMmVjLTRjNDktOTUxYS0wZjEzMWQxZjA3MTkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im9pZGMtY2xpZW50IiwiYXpwIjoib2lkYy1jbGllbnQiLCJhdXRoX3RpbWUiOjE3NDg0OTk3NTMsImlzcyI6Imh0dHA6Ly8yMC4wLjMuMTA3Ojg4ODgiLCJuYW1lIjoiYWRtaW4iLCJleHAiOjE3NDg1MDE1NjksImlhdCI6MTc0ODQ5OTc2OSwianRpIjoiMzNjNmVjMzUtMWY5ZC00ZjFiLWJmMmUtZWI1ZDU2MTNjNTVjIiwiZW1haWwiOiIxMjNAMTYzLmNvbSIsInNpZCI6ImpTaGg5c2tRVjZxYmFidUVZcjlLcjViSXhZM3VhVFBGVlhZcFFqTEVOY3MifQ.ZUqL4pqOVn2TEEpZXk9hh0UPMqUvShRNQFMugG_4n71HM_HxnHnA-M-HnvqaBTFML7dhSgU64InWP1Cgf8dSrydIkcmeFd7dXdEb10Qepg5dUbmVdVYPBNF3jD_OuwEEd89srw5WEQuF-ehg9tMzaDFPwhSsny3OXgzegHmAH5DIrgZ-vvEoH_PyK4HHFsl1d02gN0duYUFHqWFNAYNJjQu0UiHnrw8_EkFyMe5F1f1MRl-1bPMrWGehNBidtSn8IJW8bd2_9fTScMTIXGMIOV9lVythrZeowamdvW0Uf5fyBTGA_X0AD8xH2kjQywP-hyrAi3bwKonC8oG8m8-G6w",
          "token_type": "Bearer",
          "expires_in": 7200
      }
      ```

3. 调用 userinfo:

      ```bash
      # curl --location --request GET 'http://localhost:8080/userinfo' \
      --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJ0ZW5hbnRfaWQiOiIzMDFmMDNhZThmOTk0OTJkOGNmZDEyZDJlNmFjMTA3ZCIsInN1YiI6ImFkbWluIiwiY3JlYXRlZCI6MTc0ODQ5OTc2OTY0OCwic2Vzc2lvbl9pZCI6ImQ1YmU3NGY4NGU5MjQ0M2Y5MmMzNmJmYzE5OTgxYzZiIiwiaWQiOiI5MzMzZDZiZTk5Mzg0YTM5YjM0NWJlOTE0YjQzM2Q1YSIsImV4cCI6MTc0ODUwNjk2OX0.VfcxwTRZRmfjfDaTT5j3u8ND7hgD4xcInwJs68z4uWAn8CYUXrqncnFmIl-8FquyhJ1Ur9msc1YZnPMPSM_wKg'

      {
          "sub": "admin",
          "aud": [
              "oidc-client"
          ],
          "azp": "oidc-client",
          "auth_time": 1748500904277,
          "iss": "http://localhost:8080",
          "name": "admin",
          "exp": 1748502725.329161400,
          "iat": 1748500925.329161400,
          "jti": "57b88c0e-00b7-40e7-bf36-9e478bebd86a",
          "email": "123@163.com",
          "sid": "MGhGddGm26UUhtEZpwALpFNRpaCCz0-LuVvDAeYhxHM"
      }
      ```

4. 刷新 token:

      ```bash
      # curl --location --request POST 'http://localhost:8080/oauth2/token' \
      --header 'Content-Type: application/x-www-form-urlencoded' \
      --data-urlencode 'grant_type=refresh_token' \
      --data-urlencode 'refresh_token=eyJhbGciOiJIUzUxMiJ9.eyJ0ZW5hbnRfaWQiOiIzMDFmMDNhZThmOTk0OTJkOGNmZDEyZDJlNmFjMTA3ZCIsInN1YiI6ImFkbWluIiwiY3JlYXRlZCI6MTc0ODQ5OTc2OTc3MSwic2Vzc2lvbl9pZCI6IjVkNWRiYjZlMWJhNDQ0MTk5YzEzNDBlOTNkMWRlYmIxIiwiaWQiOiI1NDAwZTM2MDlkNGY0MzYzYjBiZDljZjQ3NTAwYzZmNCIsImV4cCI6MTc0ODUwNjk2OX0.b7P170BM91iBXkkWB6JsJHCgu5_FzxpWzaC-ym6b-D5Gnmw3uZPBtr0DDDa1DZSHh06mP20256fITgZfb_Tr1w' \
      --data-urlencode 'client_id=oidc-client' \
      --data-urlencode 'client_secret=admin'

      {
         "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVpZCI6IjIiLCJjcmVhdGVkIjoxNzQ3OTYyODgwMzY3LCJ1dHlwZSI6IjIiLCJpZCI6ImE2YWFlOWMwNDNiZDQ0YTFiMTlkMDdkMjk1M2E1YWQ1Iiwib2F1dGhUeXBlIjoiMSIsInRpZCI6ImI4ZTgzYTUxOWE0MTQxODU4M2JkODQ3ZThiMTA4ZjIyIiwicGxhdGZvcm0iOiJvYXV0aDIiLCJzaWQiOiI1YTY3YWFjNmRhODc0MGEyYjI4MmFjMDM0OTI1YjJmYyIsImV4cCI6MTc0Nzk3MDA4MH0.hnasFUl7VFMwLckCF1b5cKh-5AdQ3iSWKVaTtRN8OaeSKuqV0kimN-zhRt1y5tsNS4fgSFf5z6m4ewc453_fhw",
         "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVpZCI6IjIiLCJjcmVhdGVkIjoxNzQ3OTYyODcyMDkxLCJ1dHlwZSI6IjIiLCJpZCI6Ijk1YTk1Y2ZjZWFjNzRmNDc4NjdmZGQ1M2QxMzQwZTkwIiwib2F1dGhUeXBlIjoiMSIsInRpZCI6ImI4ZTgzYTUxOWE0MTQxODU4M2JkODQ3ZThiMTA4ZjIyIiwicGxhdGZvcm0iOiJvYXV0aDIiLCJzaWQiOiI4MWI4NThiNWRiODY0MzE2OTM2MDQ1ZWVmYTM3NmJjOSIsImV4cCI6MTc0Nzk3MDA3Mn0.Nu3dSDW-czW0rSBkLnORwjO4y7UWE4_qTd2FPngjrepIAtVgv8m_8o0o2wW7CCO3M4hds6jXmEBSNj0vxgOrAg",
         "scope": "openid profile",
         "id_token": "eyJraWQiOiJiOWVmYWZhMC0yNzM2LTQ1YTItODM4ZS1iNjY5YmVhYWQwOGEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im9pZGMtY2xpZW50IiwiYXpwIjoib2lkYy1jbGllbnQiLCJhdXRoX3RpbWUiOjE3NDc5NjI4NjIsImlzcyI6Imh0dHA6Ly8yMC4wLjMuMTA1Ojg4ODgiLCJuYW1lIjoiYWRtaW4iLCJleHAiOjE3NDc5NjQ2ODAsImlhdCI6MTc0Nzk2Mjg4MCwianRpIjoiNTMzNmQxYzItYjU3MC00ZThmLWEyMzYtODNkNjkzNjFlOWFkIiwiZW1haWwiOiJ4dXl1YW56aGlAc2FmdG9wLmNuIiwic2lkIjoiMnNVNWtxNW9qcmN3N1c3cUk3X1hVYk50ejZuZTNIVDljRDdvc3ZSVVBSNCJ9.AmMjuMkxnXlUHf17eTrWiFG6YfIKvP7k1JPYfFL1fKP8gVU8sfGUf1JWCom6ntA_rIQbKaopVhLhwkbS7rnyiexA2x3lyIRYzUgImswF2HSFB6RNGnjOiVCnYFT95Ezb-E7dLmMJE04UUrV4G20DQCqNco6Ue52nNf6Uvhijsuy6hM6lC3txjPIvUVNfJgDlTBEXGqStZlegxTeBwj2ERJxG-OruX3Devzd_1Y5KJRMIE_gBm1TMkj6QK9BJ7Cl_ict3UjDUbOdVmCTFg-hi3-m5PrhkXJqTCVtVquypoDScCGizxZpIpsswedcMRKYeF-2vzo6gzWfWBYxAcAoN6g",
         "token_type": "Bearer",
         "expires_in": 7200
      }
      ```
