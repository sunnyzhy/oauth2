# 授权服务

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

扩展表用于存储登录认证的用户名、密码、角色。

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

- JdbcTokenStore, JwtTokenStore, RedisTokenStore 使用的都是 JdbcClientDetailsService, 读取数据表 oauth_client_details
- InMemoryTokenStore 读取的是内存里的 client_details

## 生成 token

1. 启动授权服务: auth-server-0.0.1.jar
   ```bash
   # nohup java -jar -XX:+HeapDumpOnOutOfMemoryError -Xmx128m -Xms128m ./spring-security-oauth2-auth-server-0.0.1.jar > /dev/null &
   ```

2. 在浏览器中访问 /oauth/authorize 接口
   ```
   http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
   ```
   
3. 输入用户名、密码进行登录认证

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
