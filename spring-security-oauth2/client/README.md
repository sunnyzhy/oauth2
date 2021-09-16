# 客户端（第三方服务）

客户端，也可以称为第三方服务，即普通的业务服务。

## 数据表

./resources/db/client.sql

### 数据表

- client_authority: 角色表
- client_user: 用户表
- client_user_authority: 用户-角色表

## 客户端访问资源服务的主要流程

1. 在浏览器中访问客户端的 /authorized 接口
   ```
   http://localhost:8080/authorized
   ```

2. 浏览器重定向到**客户端**的 /login 接口

3. 输入**客户端**的用户名(admin)、密码(admin)进行认证

4. 浏览器重定向到**授权服务**的 /login 接口

5. 输入**授权服务**的用户名(admin)、密码(admin)进行认证
   
6. 浏览器重定向到客户端的 /authorized 接口
   ```
   GET http://localhost:8080/authorized?code=S8cIMO&state=SEOb0F
   
   message method is ok
   ```
