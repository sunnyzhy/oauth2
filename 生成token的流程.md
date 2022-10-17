# 生成 token 的流程 (code 换取 token)

## 1 访问 /oauth2/authorize 接口（GET）

**在浏览器中访问授权服务器的 ```/oauth2/authorize``` 接口。**

```
http://localhost:8080/oauth2/authorize?response_type=code&scope=openid+email+phone&client_id=messaging-client&redirect_uri=http://localhost
```

- ```response_type=code``` : **必须**，请求的响应类型为 ```authorization code```
- ```scope=openid``` :  **OIDC 请求的时候为必选项，其他请求的时候为可选项**，OIDC 请求时的 scope 参数值必须包含 ```openid```
- ```client_id=messaging-client``` : **必须**，在授权服务器上注册的 ```client_id```
- ```redirect_uri=http://localhost``` : **必须**，在授权服务器上注册的重定向地址

## 2 重定向到 /oauth/login 接口（GET）

**浏览器重定向到授权服务器的 ```/oauth/login``` 接口。**

**如果未认证，就重定向到授权服务器的认证接口，此时会打开 login 认证界面；否则，就直接转到步骤 6。**

```
http://localhost:8080/oauth/login
```

## 3 登录并调用 /oauth/login/authorize 接口（POST）

**输入用户名(admin)、密码(admin)，点登录之后，调用授权服务器的 ```/oauth/login/authorize``` 接口。**

```
http://localhost:8080/oauth/login/authorize
```

## 4 重定向到 /oauth2/authorize 接口（GET）

**浏览器又重定向到授权服务的 ```/oauth2/authorize``` 接口，即步骤 1 所访问的授权接口地址。**

```
http://localhost:8080/oauth2/authorize?response_type=code&scope=openid+email+phone&client_id=messaging-client&redirect_uri=http://localhost
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
   http://localhost:8080/oauth2/token?grant_type=authorization_code&code=<CODE>&client_id=messaging-client&client_secret=secret&redirect_uri=http://localhost
   ```

   示例:
   ```
   http://localhost:8080/oauth2/token?grant_type=authorization_code&code=QHu3OxrHtN64DP0fu3Nc1OkKBaV8A6tjUQ4v9Nk64mMsFWVnLWlZr2Wnr4DRTtbho64Tr814csVZTIRjbY3vbmxoKLfMR7sZIyQ6R_sp0Dt3YE4QD4NuvMr1GFE4sEQ0&client_id=messaging-client&client_secret=secret&redirect_uri=http://localhost
   ```

2. **curl:**
   ```bash
   curl --data-urlencode "grant_type=authorization_code" --data-urlencode "code=<CODE>" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" --data-urlencode "redirect_uri=http://localhost" -X POST http://localhost:8080/oauth2/token
   ```

   示例:
   ```bash
   curl --data-urlencode "grant_type=authorization_code" --data-urlencode "code=QHu3OxrHtN64DP0fu3Nc1OkKBaV8A6tjUQ4v9Nk64mMsFWVnLWlZr2Wnr4DRTtbho64Tr814csVZTIRjbY3vbmxoKLfMR7sZIyQ6R_sp0Dt3YE4QD4NuvMr1GFE4sEQ0" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" --data-urlencode "redirect_uri=http://localhost" -X POST http://localhost:8080/oauth2/token
   ```

   **or**

   ```bash
   curl -X POST -d "grant_type=authorization_code&code=<CODE>&client_id=messging-client&client_secret=secret&redirect_uri=http://localhost" http://localhost:8080/oauth2/token
   ```

   示例:
   ```bash
   curl -X POST -d "grant_type=authorization_code&code=QHu3OxrHtN64DP0fu3Nc1OkKBaV8A6tjUQ4v9Nk64mMsFWVnLWlZr2Wnr4DRTtbho64Tr814csVZTIRjbY3vbmxoKLfMR7sZIyQ6R_sp0Dt3YE4QD4NuvMr1GFE4sEQ0&client_id=messging-client&client_secret=secret&redirect_uri=http://localhost" http://localhost:8080/oauth2/token
   ```

- ```grant_type=authorization_code``` : **必须**，授权码模式(authorization code)
- ```code=QHu3OxrHtN64DP0fu3Nc1OkKBaV8A6tjUQ4v9Nk64mMsFWVnLWlZr2Wnr4DRTtbho64Tr814csVZTIRjbY3vbmxoKLfMR7sZIyQ6R_sp0Dt3YE4QD4NuvMr1GFE4sEQ0``` : **必须**，授权服务器返回的授权码
- ```client_id=messaging-client``` : **必须**，在授权服务器上注册的 client_id
- ```client_secret=secret``` : **必须**，在授权服务器上注册的 secret
- ```redirect_uri=http://localhost``` : **必须**，在授权服务器上注册的重定向的地址
   
成功获取 token 如下: 

```json
{
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2NjU0NzMyODIsImlzcyI6Imh0dHA6XC9cL2xvY2FsaG9zdDo4ODg4IiwidXR5cGUiOiIyIiwidGlkIjoiYjhlODNhNTE5YTQxNDE4NTgzYmQ4NDdlOGIxMDhmMjIiLCJhdWQiOiJtZXNzYWdpbmctY2xpZW50IiwidWlkIjoiMiIsIm5iZiI6MTY2NTQ3MzI4MiwiaWQiOiIzYzQyNjU2YTNiNTU0MjUyYWE2ZTc3NTllNzkzNmY1NCIsIm9hdXRoVHlwZSI6IjEiLCJleHAiOjE2NjU0NzY4ODIsImlhdCI6MTY2NTQ3MzI4Mn0.WQjRlSg2H0X2VkQpl9KWMYK5-7wx5wqJLNCYfwjgJU9YdXEiW-xTpraEeMACH8YnAzdyF4I-AgFIj58B8MBXCw",
    "refresh_token": "wsyn9nnBAocnPdN8L3RS_Sx2yedLvvtkvuQydW0NwX7xrBilAYuAsCVEyv86bEYrvTBXsYvRZIzx_LhBxXx1LPfc7JYU7eYOof0sqW93WVnF4n1-D9fV1Yhckd4PJsLj",
    "scope": "phone openid email",
    "id_token": "eyJraWQiOiI4MzAzMDMzNC1mNTJmLTQ2YjYtOThkOS01NWMzMjhiOTM5ZDYiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJhZGRyZXNzIjoiQ04iLCJhenAiOiJtZXNzYWdpbmctY2xpZW50IiwicHJvZmlsZSI6ImNvbS5mYXN0ZXJ4bWwuamFja3Nvbi5kYXRhYmluZC5PYmplY3RNYXBwZXJANWI4OTc1ODEiLCJpc3MiOiJodHRwOlwvXC8yMC4wLjAuNDg6ODA4MCIsInBob25lX251bWJlciI6IjEyMzQ1NiIsImV4cCI6MTY2NTk3NDM2MiwiaWF0IjoxNjY1OTcyNTYyLCJ1c2VySWQiOiIxIiwiZW1haWwiOiIxMjNAMTIzLmNvbSJ9.gX83CvvwLgScbWBMHytm7Oh5Iwzu1BGpkmrv6RV72MkSZWTx7KvYwZ322fuBxkdT_UQA1EGGCGVR71KeuDRZTA",
    "token_type": "Bearer",
    "expires_in": 3599
}
```

## 8 获取 userinfo 调用 /userinfo 接口（GET）

**调用授权服务器的 ```/userinfo``` 接口。**

1. **curl:**
   ```bash
   curl -X GET -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/userinfo
   ```

   示例:
   ```bash
   curl -X GET -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2NjU0NzMyODIsImlzcyI6Imh0dHA6XC9cL2xvY2FsaG9zdDo4ODg4IiwidXR5cGUiOiIyIiwidGlkIjoiYjhlODNhNTE5YTQxNDE4NTgzYmQ4NDdlOGIxMDhmMjIiLCJhdWQiOiJtZXNzYWdpbmctY2xpZW50IiwidWlkIjoiMiIsIm5iZiI6MTY2NTQ3MzI4MiwiaWQiOiIzYzQyNjU2YTNiNTU0MjUyYWE2ZTc3NTllNzkzNmY1NCIsIm9hdXRoVHlwZSI6IjEiLCJleHAiOjE2NjU0NzY4ODIsImlhdCI6MTY2NTQ3MzI4Mn0.WQjRlSg2H0X2VkQpl9KWMYK5-7wx5wqJLNCYfwjgJU9YdXEiW-xTpraEeMACH8YnAzdyF4I-AgFIj58B8MBXCw" http://localhost:8080/userinfo
   ```

- ```Authorization: Bearer <ACCESS_TOKEN>``` : **必须**，传递 ```ACCESS_TOKEN```

成功获取 userinfo 如下：

```json
{
  "sub": "admin",
  "phone_number": "123456",
  "email": "123@123.com"
}
```

## 9 刷新 token 调用 /oauth2/token 接口（POST）

**调用授权服务器的 ```/oauth2/token``` 接口。注意: ```grant_type=refresh_token```。**

1. **postman:**
   ```bash
   http://localhost:8080/oauth2/token?grant_type=refresh_token&refresh_token=<REFRESH_TOKEN>&client_id=messaging-client&client_secret=secret
   ```

   示例:
   ```
   http://localhost:8080/oauth2/token?grant_type=refresh_token&refresh_token=wsyn9nnBAocnPdN8L3RS_Sx2yedLvvtkvuQydW0NwX7xrBilAYuAsCVEyv86bEYrvTBXsYvRZIzx_LhBxXx1LPfc7JYU7eYOof0sqW93WVnF4n1-D9fV1Yhckd4PJsLj&client_id=messaging-client&client_secret=secret
   ```

2. **curl:**
   ```bash
   curl --data-urlencode "grant_type=refresh_token" --data-urlencode "refresh_token=<REFRESH_TOKEN>" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" -X POST http://localhost:8080/oauth2/token
   ```

   示例:
   ```bash
   curl --data-urlencode "grant_type=refresh_token" --data-urlencode "refresh_token=wsyn9nnBAocnPdN8L3RS_Sx2yedLvvtkvuQydW0NwX7xrBilAYuAsCVEyv86bEYrvTBXsYvRZIzx_LhBxXx1LPfc7JYU7eYOof0sqW93WVnF4n1-D9fV1Yhckd4PJsLj" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" -X POST http://localhost:8080/oauth2/token
   ```

   **or**

   ```bash
   curl -X POST -d "grant_type=refresh_token&refresh_token=<REFRESH_TOKEN>&client_id=messaging-client&client_secret=secret" http://localhost:8080/oauth2/token
   ```

   示例:
   ```bash
   curl -X POST -d "grant_type=refresh_token&refresh_token=wsyn9nnBAocnPdN8L3RS_Sx2yedLvvtkvuQydW0NwX7xrBilAYuAsCVEyv86bEYrvTBXsYvRZIzx_LhBxXx1LPfc7JYU7eYOof0sqW93WVnF4n1-D9fV1Yhckd4PJsLj&client_id=messaging-client&client_secret=secret" http://localhost:8080/oauth2/token
   ```

- ```grant_type=refresh_token``` : **必须**，刷新 token
- ```refresh_token=wsyn9nnBAocnPdN8L3RS_Sx2yedLvvtkvuQydW0NwX7xrBilAYuAsCVEyv86bEYrvTBXsYvRZIzx_LhBxXx1LPfc7JYU7eYOof0sqW93WVnF4n1-D9fV1Yhckd4PJsLj``` : **必须**，获取 token 时，授权服务器返回的 refresh_token
- ```client_id=messaging-client``` : **必须**，在授权服务器上注册的 client_id
- ```client_secret=secret``` : **必须**，在授权服务器上注册的 secret

刷新 token 如下：

```json
{
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2NjU0NzM0NTQsImlzcyI6Imh0dHA6XC9cL2xvY2FsaG9zdDo4ODg4IiwidXR5cGUiOiIyIiwidGlkIjoiYjhlODNhNTE5YTQxNDE4NTgzYmQ4NDdlOGIxMDhmMjIiLCJhdWQiOiJtZXNzYWdpbmctY2xpZW50IiwidWlkIjoiMiIsIm5iZiI6MTY2NTQ3MzQ1NCwiaWQiOiIwYTdiM2M2ZmMyODI0NDYwOTIzNmE4ZDE4MDZjMDA1YyIsIm9hdXRoVHlwZSI6IjEiLCJleHAiOjE2NjU0NzcwNTQsImlhdCI6MTY2NTQ3MzQ1NH0.j3Jfm9GyZXRkp5Sm-14iYaFTK7EGxpFci-4WvtioOJsQEAHUHAeT4Gxj1PY7UMIo7Xi7EOTIEt_xvbf-qgyK6A",
    "refresh_token": "wsyn9nnBAocnPdN8L3RS_Sx2yedLvvtkvuQydW0NwX7xrBilAYuAsCVEyv86bEYrvTBXsYvRZIzx_LhBxXx1LPfc7JYU7eYOof0sqW93WVnF4n1-D9fV1Yhckd4PJsLj",
    "scope": "phone openid email",
    "id_token": "eyJraWQiOiI4MzAzMDMzNC1mNTJmLTQ2YjYtOThkOS01NWMzMjhiOTM5ZDYiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJhZGRyZXNzIjoiQ04iLCJhenAiOiJtZXNzYWdpbmctY2xpZW50IiwicHJvZmlsZSI6ImNvbS5mYXN0ZXJ4bWwuamFja3Nvbi5kYXRhYmluZC5PYmplY3RNYXBwZXJANWI4OTc1ODEiLCJpc3MiOiJodHRwOlwvXC8yMC4wLjAuNDg6ODA4MCIsInBob25lX251bWJlciI6IjEyMzQ1NiIsImV4cCI6MTY2NTk3NDM2MiwiaWF0IjoxNjY1OTcyNTYyLCJ1c2VySWQiOiIxIiwiZW1haWwiOiIxMjNAMTIzLmNvbSJ9.gX83CvvwLgScbWBMHytm7Oh5Iwzu1BGpkmrv6RV72MkSZWTx7KvYwZ322fuBxkdT_UQA1EGGCGVR71KeuDRZTA",
    "token_type": "Bearer",
    "expires_in": 3600
}
```
