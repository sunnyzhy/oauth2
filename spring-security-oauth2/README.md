# spring-security-oauth2

## 服务简介

- auth-server: [授权服务](./auth-server/README.md)

- client: [客户端](./client/README.md)

- resource-server: [资源服务](./resource-server/README.md)

## IMPORTANT

Make sure to modify your ```/etc/hosts``` file to avoid problems with session cookie overwrites between ```client``` and ```auth-server```. Simply add the entry ```127.0.0.1 auth-server```

## 数据表

- auth-server: [oauth2.sql](./auth-server/src/main/resources/db/oauth2.sql)

- client: [client.sql](./client/src/main/resources/db/client.sql)

## 用户名和密码

- auth-server
   ```
   Go to http://localhost:8090/login using admin/admin
   ```

- client
   ```
   Go to http://localhost:8080/login using user/user
   ```
