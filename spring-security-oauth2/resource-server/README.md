# 资源服务

示例中的资源服务有两个 HTTP 接口:

- /message: 需要认证授权
- /product: 不需要认证授权

## 请求 /message

1. 请求 http://localhost:8092/message

2. 跳转到授权服务的身份认证接口 http://localhost:8090/login

3. 身份认证成功之后，授权服务返回 code

4. 用 code 通过授权服务换取 access_token
   ```json
   {
        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IllLeDFWTDlNL0RDT3ZOWCt0OWxUQXJpUXJtVlhtSDl6b3FQQWFnMDh4U2s9In0.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsiMzAiXSwiaWQiOiIyMGRjOTNjNGYzN2Y0NzVhODExZTUyMTMwYjBlYjBiOCIsImV4cCI6MTYzMTcxMzM4MywiYXV0aG9yaXRpZXMiOlsiVVNFUiIsIkFETUlOIl0sImp0aSI6ImQyNzYyNDkzLWFjNGYtNDQyMi1iMDM0LWNiZjU1Y2FhYmNmZiIsImNsaWVudF9pZCI6Im1lc3NhZ2luZy1jbGllbnQifQ.C7SRAxnp9I5_5YhDrrqRSzvtCsB__k3erFS1BQN8E2QlM4zQpGLfYtNtUPjhlcc2fZ1yo4YGXhxdcAObspXgXPuQkZIya6_kkyD0WzrI8WFr3GzjWBxHHVs9Go9zM39RCwSOE2eVUiqVz8zFHdmZAI0rlBhwVBFYVWlmwJ4wPqltHpGj2ZYzTmThj3Mj8E34K1DhKk2VzQVr1RmB5V08nQqQEKLFQOAZDG6t7ehpkConAW4m6nJ-EkczZUX8fhu5WA8yJftSYP7KEjGl8m0Zkb4M8Afjpw8N5LrrH4aZC0u2MkucsDN3E6rAk8R8QwJUIw-n79SH5bYyg4-MeBduEw",
        "token_type": "bearer",
        "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IllLeDFWTDlNL0RDT3ZOWCt0OWxUQXJpUXJtVlhtSDl6b3FQQWFnMDh4U2s9In0.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsiMzAiXSwiYXRpIjoiZDI3NjI0OTMtYWM0Zi00NDIyLWIwMzQtY2JmNTVjYWFiY2ZmIiwiaWQiOiIyMGRjOTNjNGYzN2Y0NzVhODExZTUyMTMwYjBlYjBiOCIsImV4cCI6MTYzMTcxMzM4MywiYXV0aG9yaXRpZXMiOlsiVVNFUiIsIkFETUlOIl0sImp0aSI6Ijc2M2QwNjY1LTlkZTUtNDdiMS05OGQ3LTM0NWU4ZTIxMjRkZCIsImNsaWVudF9pZCI6Im1lc3NhZ2luZy1jbGllbnQifQ.YjBie37Np4_WR_O84u8ulHgiBsnFmUe9ImWnzNIpv6f06jmQ93kY8f1xInLIaYEvRPXrRO_X9-WjpEz1eGi9p0Ve6zn8CFiJxA9DqOiydmhwwNbMNHzW4BSVCZcxGT1bSFayPsYJO1wzfwFLZoryRKb_AwZpQER7K8GdU6vjZB5MBQzk75xfLgjOx8BjxRJwtu5Bqx6f49NT7zlO_ceSgCPiHUez18qxzLXzH8VbErcSddYNsECSqQdfdkbk-kPUksllysqz0TQvOEtKepbxDxwHtMBretbcn7JWhPgtZxEZeb48VVFxxRxFYod_R5TUyL6v0WhUcCJOudCNtsHqQQ",
        "expires_in": 24460,
        "scope": "30",
        "sub": "admin",
        "id": "20dc93c4f37f475a811e52130b0eb0b8",
        "jti": "d2762493-ac4f-4422-b034-cbf55caabcff"
   }
   ```

5. 请求 http://localhost:8092/message 的时候需要携带 access_token (GET)
   - Postman - Params 方式，在环境变量的下拉列表中选择 "No Environment"
      ```
      http://localhost:8092/message?access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IllLeDFWTDlNL0RDT3ZOWCt0OWxUQXJpUXJtVlhtSDl6b3FQQWFnMDh4U2s9In0.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsiMzAiXSwiaWQiOiIyMGRjOTNjNGYzN2Y0NzVhODExZTUyMTMwYjBlYjBiOCIsImV4cCI6MTYzMTcxMzM4MywiYXV0aG9yaXRpZXMiOlsiVVNFUiIsIkFETUlOIl0sImp0aSI6ImQyNzYyNDkzLWFjNGYtNDQyMi1iMDM0LWNiZjU1Y2FhYmNmZiIsImNsaWVudF9pZCI6Im1lc3NhZ2luZy1jbGllbnQifQ.C7SRAxnp9I5_5YhDrrqRSzvtCsB__k3erFS1BQN8E2QlM4zQpGLfYtNtUPjhlcc2fZ1yo4YGXhxdcAObspXgXPuQkZIya6_kkyD0WzrI8WFr3GzjWBxHHVs9Go9zM39RCwSOE2eVUiqVz8zFHdmZAI0rlBhwVBFYVWlmwJ4wPqltHpGj2ZYzTmThj3Mj8E34K1DhKk2VzQVr1RmB5V08nQqQEKLFQOAZDG6t7ehpkConAW4m6nJ-EkczZUX8fhu5WA8yJftSYP7KEjGl8m0Zkb4M8Afjpw8N5LrrH4aZC0u2MkucsDN3E6rAk8R8QwJUIw-n79SH5bYyg4-MeBduEw
      ```

      ![Params](../../images/token/token-01.png 'Params')

   - Postman - Authorization 方式，在环境变量的下拉列表中选择已经配置好的 "Token Environment"
      1. 添加环境变量 access_token

         ![Environment](../../images/token/token-02.png 'Environment')
      2. 引用环境变量 {{access_token}}, 并发送请求

         ![Authorization](../../images/token/token-03.png 'Authorization')

## 请求 /product

```
GET http://localhost:8092/product

product method is ok
```
