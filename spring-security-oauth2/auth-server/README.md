# 授权服务

授权服务包含认证与授权两个流程:

1. 认证: 由 Spring-Security 负责
2. 授权: 由 Oauth2 流程

## 数据表

./resources/db/oauth2.sql

### oauth2 表

- clientdetails
- oauth_access_token
- oauth_approvals
- oauth_client_details
- oauth_client_token
- oauth_code
- oauth_refresh_token

### oauth2 扩展表

- oauth_extend_authority
- oauth_extend_user_authority
- oauth_extend_user_details

扩展表用于存储认证的用户名、密码、角色。

## token 存储方式

### TokenStore

- InMemoryTokenStore
- JdbcTokenStore
- JwtTokenStore
- RedisTokenStore


### 示例代码里的 TokenStoreStrategy

```java
public enum TOKEN_STORE_STRATEGY {
    MEMORY(0, "memory", "InMemoryTokenStore"),
    JDBC(1, "jdbc", "JdbcTokenStore"),
    JWT(2, "jwt", "JwtTokenStore"),
    REDIS(3, "redis", "RedisTokenStore");
}
```

### 通过修改 application.yml 来指定 token 的存储方式

```yml
oauth:
  token:
    store:
      strategy: JDBC # MEMORY,JDBC,JWT,REDIS
```

### 关于示例代码里的 ClientDetailsService

- JdbcTokenStore, JwtTokenStore, RedisTokenStore 模式下的 ClientDetailsService 使用的都是 JdbcClientDetailsService, 读取数据表 oauth_client_details
- InMemoryTokenStore 模式下的 ClientDetailsService 读取的是内存里的 client_details

## 生成 token

1. 启动授权服务
   ```bash
   # nohup java -jar -XX:+HeapDumpOnOutOfMemoryError -Xmx128m -Xms128m ./spring-security-oauth2-auth-server-0.0.1.jar > /dev/null &
   ```

2. 在浏览器中访问 /oauth/authorize 接口
   ```
   http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
   ```
   
3. 输入用户名(admin)、密码(admin)进行认证

4. 授权（如果需要授权的话，否则跳过该步骤）

5. 浏览器返回 code
   ```
   http://localhost?code=W636oE
   ```
   
6. 生成 token

   - postman
      ```
      http://localhost:8090/oauth/token?grant_type=authorization_code&code=W636oE&client_id=messaging-client&client_secret=secret&redirect_uri=http://localhost
      ```

   - curl
      ```bash
      # curl --data-urlencode "grant_type=authorization_code" --data-urlencode "code=W636oE" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" --data-urlencode "redirect_uri=http://localhost" -X POST http://localhost:8090/oauth/token
      ```

   - curl
      ```bash
      # curl -X POST -d "grant_type=authorization_code&code=W636oE&client_id=messging-client&client_secret=secret&redirect_uri=http://localhost" http://localhost:8090/oauth/token
      ```

## 前后端分离配置

### 示例代码里的 FRONT_BACK_SEPARATION

```java
public enum FRONT_BACK_SEPARATION {
    BACK_END(0, "back_end", "前后端不分离"),
    FRONT_END(1, "front_end", "前后端分离");
}
```

### 通过修改 application.yml 来配置是否前后端分离

```yml
oauth:
  front-and-back-separation: FRONT_END # FRONT_END: 前后端分离, 使用自定义的认证页面; BACK_END: 使用 spring-security 默认的认证页面
```

## 跨域配置

### 示例代码里的 CORS_STRATEGY

```java
public enum CORS_STRATEGY {
    SERVER(0, "SERVER", "服务端实现跨域"),
    NGINX(1, "NGINX", "Nginx代理实现跨域");
}
```

### 通过修改 application.yml 来配置跨域策略

```yml
cors:
  strategy: NGINX # SERVER:服务端实现跨域; NGINX:Nginx代理实现跨域
```

### Nginx 配置代理跨域

```conf
location /auth {
    add_header Access-Control-Allow-Origin *;
    add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
    add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';
    if ($request_method = 'OPTIONS') {
        return 200;
    }

    proxy_pass http://localhost:8090;
}
```
