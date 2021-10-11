## 简介

- 本文基于 spring boot + oauth2
- 依赖项
   ```xml
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-security</artifactId>
          <version>2.2.5.RELEASE</version>
      </dependency>

      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-oauth2</artifactId>
          <version>2.2.5.RELEASE</version>
      </dependency>
   ```

## 支持前后端分离

### 前后端不分离(使用 spring-security 默认的认证页面)

1. 修改 spring-security-oauth2\auth-server 的配置文件为使用 spring-security 默认的认证页面
   ```yml
   oauth:
     front-and-back-separation: BACK_END # FRONT_END: 前后端分离, 使用自定义的认证页面; BACK_END: 使用 spring-security 默认的认证页面
   ```

2. 编译启动 auth-server
   ```bash
   # cd spring-security-oauth2
   # mvn install
   # cd auth-server
   # mvn clean
   # mvn -DskipTests=true package
   # java -jar ./target/spring-security-oauth2-auth-server-0.0.1.jar
   ```

3. 在浏览器地址栏里输入以下 url:
   ```
   http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
   ```

4. 浏览器地址跳转到 ```http://localhost:8090/login```
   
   ![01.png](./images/oauth2/back-end/01.png '01.png')

5. 输入用户名/密码 (admin/admin) 并登录

6. 浏览器地址返回 code:
   ```
   http://localhost/?code=L0ijm3
   ```

7. 获取 token
   
   ![02.png](./images/oauth2/back-end/02.png '02.png')

### 前后端分离

1. 启动前端
   - 方法 1
      ```bash
      # cd spring-security-oauth2-ui
      # cnpm install
      # npm run dev
       I  Your application is running here: http://localhost:8088
      ```
   - 方法 2
      ```bash
      # cd spring-security-oauth2-ui
      # cnpm install
      # npm run build
      # mv dist /usr/local/nginx/html
      ```

2. 配置 nginx 代理跨域
   ```bash
   # vim /usr/local/nginx/conf/nginx.conf
   location /auth {
       add_header Access-Control-Allow-Origin *;
       add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
       add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';
       if ($request_method = 'OPTIONS') {
           return 200;
       }
   
       proxy_pass http://localhost:8090;
   }
   
   # systemctl restart nginx
   ```

3. 修改 spring-security-oauth2\auth-server 的配置文件为前后端分离
   ```yml
   oauth:
     front-and-back-separation: FRONT_END # FRONT_END: 前后端分离, 使用自定义的认证页面; BACK_END: 使用 spring-security 默认的认证页面
   ```

4. 编译启动 auth-server
   ```bash
   # cd spring-security-oauth2
   # mvn install
   # cd auth-server
   # mvn clean
   # mvn -DskipTests=true package
   # java -jar ./target/spring-security-oauth2-auth-server-0.0.1.jar
   ```

5. 在浏览器地址栏里输入以下 url:
   ```
   http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
   ```

6. 浏览器地址跳转到 ```http://localhost:8088/auth/login```
   
   ![01.png](./images/oauth2/front-end/01.png '01.png')

7. 输入用户名/密码 (admin/admin) 并登录
   
   ![02.png](./images/oauth2/front-end/02.png '02.png')

8. 获取 code
   
   ![03.png](./images/oauth2/front-end/03.png '03.png')

9. 获取 token
   
   ![04.png](./images/oauth2/front-end/04.png '04.png')
