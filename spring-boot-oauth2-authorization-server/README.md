# Spring Authorization Server

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
curl -X POST  -H "Content-Type: application/json" -d '{"clientId":"messaging-client","clientSecret":"secret","clientName":"messaging-client","redirectUri":"https://www.baidu.com"}' "http://localhost:8080/client"
```

## 获取 token

1. 获取 code: 在浏览器的地址栏输入 ```http://localhost:8080/oauth2/authorize?response_type=code&client_id=messaging-client&redirect_uri=https://www.baidu.com```, ```用户名/密码:admin/admin```

2. 获取 token:
   ```bash
   curl -X POST -d "grant_type=authorization_code&code=上述生成的code&client_id=messaging-client&client_secret=secret&redirect_uri=https://www.baidu.com" http://localhost:8080/oauth2/token
   ```

3. 刷新 token:

   ```bash
   curl -X POST -d "grant_type=refresh_token&refresh_token=上述生成的refresh_token&client_id=messaging-client&client_secret=secret" http://localhost:8080/oauth2/token
   ```
