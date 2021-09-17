# 客户端(第三方服务)访问 resource 的流程

## 1 在浏览器中访问客户端的测试接口（GET）
```
http://localhost:8080/authorized
```

## 2 浏览器重定向到```客户端```的 /login 接口（GET）
```
http://localhost:8080/login
```

## 3 输入```客户端```的用户名(user)、密码(user)进行认证（POST）
```
http://localhost:8080/login
```

## 4 浏览器重定向到客户端的测试接口（GET）
```
http://localhost:8080/authorized
```

## 5 浏览器重定向到授权服务的 /oauth/authorize 接口（GET）
```
http://auth-server:8090/oauth/authorize?client_id=messaging-client&redirect_uri=http://localhost:8080/authorized&response_type=code&scope=read%20write&state=lvo5NL
```
   
## 6 浏览器重定向到```授权服务```的 /login 接口（GET）
```
http://auth-server:8090/login
```

## 7 输入```授权服务```的用户名(admin)、密码(admin)进行认证（POST）
```
http://auth-server:8090/login
```
   
## 8 浏览器重定向到授权服务的 /oauth/authorize 接口（GET）
```
http://auth-server:8090/oauth/authorize?client_id=client&redirect_uri=http://localhost:8080/authorized&response_type=code&scope=read%20write&state=SEOb0F
```

## 9 浏览器重定向到授权服务的 /oauth/authorize 接口（POST）

**如果不满足自动授权的条件，就执行本步骤 9; 否则，就直接转到步骤 10。**

**点授权之后，调用授权服务的 /oauth/authorize 接口。**

```
http://auth-server:8090/oauth/authorize
```

## 10 浏览器重定向到客户端的测试接口（GET）
```
GET http://localhost:8080/authorized?code=S8cIMO&state=SEOb0F

message method is ok
```
