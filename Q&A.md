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
