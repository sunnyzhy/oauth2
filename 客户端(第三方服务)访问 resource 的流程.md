# 客户端(第三方服务)访问 resource 的流程

1. 在浏览器中访问客户端的 /authorized 接口（GET）
   ```
   http://localhost:8080/authorized
   ```

2. 此时，浏览器会重定向到客户端的 /login 接口（GET）
   ```
   http://localhost:8080/login
   ```

3. 输入**客户端**的用户名(admin)、密码(admin)进行认证（POST）
   ```
   http://localhost:8080/login
   ```

4. 浏览器重定向到客户端的 /authorized 接口（GET）
   ```
   http://localhost:8080/authorized
   ```

5. 浏览器重定向到授权服务的 /oauth/authorize 接口（GET）
   ```
   http://auth-server:8090/oauth/authorize?client_id=messaging-client&redirect_uri=http://localhost:8080/authorized&response_type=code&scope=read%20write&state=lvo5NL
   ```
   
6. 浏览器重定向到授权服务的 /login 接口（GET）
   ```
   http://auth-server:8090/login
   ```

7. 输入**授权服务**的用户名(admin)、密码(admin)进行认证（POST）
   ```
   http://auth-server:8090/login
   ```
   
8. 浏览器重定向到授权服务的 /oauth/authorize 接口（GET）
   ```
   http://auth-server:8090/oauth/authorize?client_id=client&redirect_uri=http://localhost:8080/authorized&response_type=code&scope=read%20write&state=SEOb0F
   ```

9. 浏览器重定向到客户端的 /authorized 接口（GET）
   ```
   GET http://localhost:8080/authorized?code=S8cIMO&state=SEOb0F
   ```
