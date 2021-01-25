# code换取token的流程
## 1. 在浏览器中访问授权服务器的 /oauth/authorize 接口（GET）
```
http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
```

- response_type=code : 必须，请求的响应类型为 authorization code
- client_id=messaging-client : 必须，在授权服务器上注册的 client_id
- redirect_uri=http://localhost : 必须，在授权服务器上注册的重定向的地址

## 2. 浏览器重定向到授权服务器的 /login 接口（GET）
```
http://localhost:8090/login
```

## 3. 输入用户名、密码，点登录之后，调用授权服务器的 /login 接口（POST）
```
http://localhost:8090/login
```

## 4. 浏览器又重定向到授权服务器的 /oauth/authorize 接口（GET）
```
http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
```

## 5. 点授权之后，调用授权服务器的 /oauth/authorize 接口（POST）
```
http://localhost:8090/oauth/authorize
```

## 6. 浏览器重定向到
```
http://localhost?code=W636oE
```

- code=W636oE : code 就是授权码

## 7. 访问授权服务器的 /oauth/token 接口（POST）
1. **postman:**
   ```
   http://localhost:8090/oauth/token?grant_type=authorization_code&code=W636oE&client_id=messaging-client&client_secret=secret&redirect_uri=http://localhost
   ```

2. **curl:**
   ```bash
   # curl --data-urlencode "grant_type=authorization_code" --data-urlencode "code=W636oE" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" --data-urlencode "redirect_uri=http://localhost" -X POST http://localhost:8090/oauth/token
   ```

   **or**

   ```bash
   # curl -X POST -d "grant_type=authorization_code&code=W636oE&client_id=messging-client&client_secret=secret&redirect_uri=http://localhost" http://localhost:8090/oauth/token
   ```

- grant_type=authorization_code : 必须，授权码模式(authorization code)
- code=W636oE : 必须，授权服务器返回的授权码
- client_id=messaging-client : 必须，在授权服务器上注册的 client_id
- client_secret=secret : 必须，在授权服务器上注册的 secret
- redirect_uri=http://localhost : 必须，在授权服务器上注册的重定向的地址
   
成功获取 token 如下: 

```json
{
    "access_token": "a2o1msDxQlL1Ej7YRJF5zmYsL_A",
    "token_type": "bearer",
    "refresh_token": "uIOk2gzINbjYc5SWBvvId_xXWK0",
    "expires_in": 43199,
    "scope": "read write"
}
```

## 8. 刷新token（POST）
1. **postman:**
   ```
   http://localhost:8090/oauth/token?grant_type=refresh_token&refresh_token=uIOk2gzINbjYc5SWBvvId_xXWK0&client_id=messaging-client&client_secret=secret
   ```

2. **curl:**
   ```bash
   # curl --data-urlencode "grant_type=refresh_token" --data-urlencode "refresh_token=uIOk2gzINbjYc5SWBvvId_xXWK0" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" -X POST http://localhost:8090/oauth/token
   ```

- grant_type=refresh_token : 必须，刷新 token
- refresh_token=uIOk2gzINbjYc5SWBvvId_xXWK0 : 必须，获取 token 时，授权服务器返回的 refresh_token
- client_id=messaging-client : 必须，在授权服务器上注册的 client_id
- client_secret=secret : 必须，在授权服务器上注册的 secret

刷新 token 如下：

```json
{
    "access_token": "gSS2GYAKNGCVLCI02Z3sDPsWMQA",
    "token_type": "bearer",
    "refresh_token": "uIOk2gzINbjYc5SWBvvId_xXWK0",
    "expires_in": 43199,
    "scope": "read write"
}
```
