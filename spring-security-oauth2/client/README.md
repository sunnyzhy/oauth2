# 客户端（第三方服务）

客户端，也可以称为第三方服务，即普通的业务服务。

## 数据表

./resources/db/client.sql

### 数据表

- client_authority: 角色表
- client_user: 用户表
- client_user_authority: 用户-角色表

## 客户端访问资源服务的主要流程

1. 启动授权服务
   ```bash
   # nohup java -jar -XX:+HeapDumpOnOutOfMemoryError -Xmx128m -Xms128m ./spring-security-oauth2-auth-server-0.0.1.jar > /dev/null &
   ```

2. 启动资源服务
   ```bash
   # nohup java -jar -XX:+HeapDumpOnOutOfMemoryError -Xmx128m -Xms128m ./spring-security-oauth2-resource-server-0.0.1.jar > /dev/null &
   ```

3. 启动客户端
   ```bash
   # nohup java -jar -XX:+HeapDumpOnOutOfMemoryError -Xmx128m -Xms128m ./spring-security-oauth2-client-0.0.1.jar > /dev/null &
   ```

4. 在浏览器中访问客户端的测试接口
   ```
   http://localhost:8080/authorized
   ```

5. 浏览器重定向到```客户端```的 /login 接口

6. 输入```客户端```的用户名(user)、密码(user)进行认证

7. 浏览器重定向到```授权服务```的 /login 接口

8. 输入```授权服务```的用户名(admin)、密码(admin)进行认证
   
9. 浏览器重定向到客户端的测试接口
   ```
   GET http://localhost:8080/authorized?code=S8cIMO&state=SEOb0F
   
   message method is ok
   ```
