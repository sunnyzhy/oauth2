# FAQ

## 1. OAuth Error
```
error="invalid_grant", error_description="Invalid redirect: http://192.168.0.10:8080/authorized does not match one of the registered values."
```

### 异常操作
在浏览器访问下面的地址:

```
http://192.168.0.10:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://192.168.0.10:8080/authorized
```

### 解决方案
**确认重定向地址的协议、域名、端口是否与在认证服务器注册的重定向地址一致。**

假如:

```java
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
            .withClient("messaging-client")
            .authorizedGrantTypes("authorization_code")
            .scopes("message.read", "message.write")
            .secret("{noop}secret")
            .redirectUris("http://localhost:8080/authorized");
}
```

在认证服务器注册的重定向地址是 "http://localhost:8080/authorized" ，所以在浏览器中应输入以下地址:

```
http://192.168.0.10:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost:8080/authorized
```

## 2. 访问 /oauth/token 时候报 401 authentication is required
**/oauth/token:**
- 如果配置了 allowFormAuthenticationForClients 且 url 中有 client_id 和 client_secret，就会用 ClientCredentialsTokenEndpointFilter 认证
- 如果配置了 allowFormAuthenticationForClients 但是 url 中没有 client_id 和 client_secret；或者没有配置 allowFormAuthenticationForClients 但是 url 中有 client_id 和 client_secret ，都会用 basic 认证

```bash
# curl --data-urlencode "grant_type=authorization_code" --data-urlencode "code=RoGgxd" --data-urlencode "client_id=messaging-client" --data-urlencode "client_secret=secret" --data-urlencode "redirect_uri=https://www.baidu.com" -X POST http://localhost:8090/oauth/token
```

所以，如果通过以上方式调用 /oauth/token 的时候，就需要在自定义的 AuthorizationServerConfigurerAdapter 类里增加以下配置中的一种:

- 配置 1
   ```java
   @Override
   public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
       security
               .tokenKeyAccess("permitAll()")
               .checkTokenAccess("permitAll()")
               .allowFormAuthenticationForClients();
   }
   ```

- 配置 2
   ```java
   @Override
   public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
       security.allowFormAuthenticationForClients();
   }
   ```
