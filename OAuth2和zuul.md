# OAuth2 和 zuul

## 概述

OAuth2 和 zuul 结合使用的时候，OAuth2 作为认证授权服务(集群部署)，zuul 作为网关服务，需要注意以下两点:

1. 对 client 的增、删、必、查操作走 zuul (也可以走 nginx，但是由于不受 session 限制，所以走 zuul)
2. 对 client 的认证授权操作走 nginx

nginx.conf

```conf
http {
   upstream  lb-oauth {
       server    192.168.0.10:8090;
       server    192.168.0.11:8090;
   }

   server {
       listen       80;
       server_name  localhost;

       location /oauth {
        proxy_pass http://lb-oauth;
        proxy_redirect default;
      }

    }

}
```

获取授权码:

```
http://localhost/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
```

## 原理分析

### 1 cookie/session

介绍:

- cookie：cookie 是一小段文本信息(单个 cookie 保存的数据不能超过 4K，很多浏览器都限制一个站点最多保存 20 个 cookie)，存放在客户浏览器上。客户端请求服务器，如果服务器需要记录该用户状态，就使用 response 向客户端浏览器颁发一个 cookie。客户端会把 cookie 保存起来。当浏览器再请求该网站时，浏览器把请求的网址连同该 cookie 一同提交给服务器。服务器检查该 cookie，以此来辨认用户状态。
- session：服务器没有对 session 存储数量的限制，session 可以保存更为复杂的数据类型，存放在服务器端。客户端浏览器访问服务器的时候，服务器把客户端信息以某种形式记录在服务器上，这就是 session。在创建 session 的时候通常也会向客户端颁发 cookie，cookie 的值就是该 session id。客户端浏览器再次访问时携带 cookie 中 session id，从而可以在服务器中找到想要的 session 信息，同时能标识当前客户端用户的身份。

特性:

- cookie: cookie 具有不可跨域名性。根据 cookie 规范，浏览器访问 google 只会携带 google 的 cookie，而不会携带 baidu 的 cookie。google 也只能操作 google 的 cookie，而不能操作 baidu 的 cookie。cookie 在客户端是由浏览器来管理的。浏览器能够保证 google 只会操作 google 的 cookie，而不会操作 baidu 的 cookie，从而保证用户的隐私安全。浏览器判断一个网站是否能操作另一个网站 cookie 的依据是域名。如果域名不一样，就不能操作彼此的 cookie。
- session: session 机制决定了当前用户只会获取到自己的 session，而不会获取到别人的 session。各用户的 session 也彼此独立，互不可见，相互之间不可访问。

以用户访问某个系统为例:

1. 用户 A 登陆某个系统，在进行完身份认证后，该系统会在服务器上为这个用户创建一个 session，并将认证后的用户信息保存到 session 里。
2. 服务器在创建session的同时会创建一个 key（可在 spring 配置文件中设置属性 server.servlet.session.cookie.name ），value 为该 session id 的 cookie 响应给浏览器，浏览器保存该 cookie。
3. 用户 A 在后续请求的时候，浏览器就会携带该 cookie，服务器就可以通过请求中的 cookie 来判定这些请求是不是用户 A 发送的。

### 2 Spring OAuth 认证流程

1. 用户首次访问某个服务时，浏览器端会携带 cookie 访问服务端，此时，服务端没有对应的 session id 或服务端没有找到该 session id 对应的有效 session，就跳转到登录页。
2. 用户在浏览器端登录成功后，服务端会创建用户 session，并把保存有 session id 的 cookie 返回给浏览器，浏览器保存该 cookie，用户在后续访问该服务时，浏览器都会携带该 cookie。
3. 当用户在浏览器端再次访问该服务时，浏览器会携带之前保存的 cookie。服务端通过 session id 就能找到该用户的 session，所以不需要再次登录。
4. 注销时请求的 url 和登录的 url 是同一域名，浏览器同样会携带该 cookie，服务端找到该 session id 对应的 session，将其清空失效，然后重定向。

当 OAuth2 集群部署的时候，就会出现以下问题:

1. 由于 session 是保存在服务器端的，当浏览器第一次请求时，被 nginx 转发到服务器 A，服务器 A 创建 session(A)，同时把保存有 session id(A) 的 cookie 响应给浏览器。
2. 当浏览器携带 cookie 再次请求时，可能被 nginx 转发到服务器 B，此时服务器 B 无法找到 cookie 中 session id(A) 所对应的 session(A)，就会返回登录页，浏览器端就需要重新登录。
3. 当浏览器重新登录之后，服务器 B 会创建另一个 session(B)，同时把保存有 session id(B) 的 cookie 响应给浏览器。循环往复，如果 nginx 轮训策略设置的够巧妙，可能永远都无法实现单点登录，除非只保留一个 oauth server 运行。

### 3 session 共享

解决的办法就是将原本存放在各自服务器上的 session 存放在一个公共的存储介质中，比如 redis 和 mysql。当请求的并发量比较高的时候，采用 redis 作为存储介质会比较合适。

### 3.1 redis 实现

pom.xml

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
```

application.yml

```yml
spring:
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
    password: password
    timeout: 5000
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1
```

### 3.2 mysql 实现

pom.xml

```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-jdbc</artifactId>
        </dependency>
```

application.yml

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://127.0.0.1:3306/spring_session?characterEncodeing=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai
    username: username
    password: password
    druid:
      filter:
        stat:
          enabled: false
      initial-size: 1
      min-idle: 1
      max-active: 20
      test-on-borrow: true
```

数据库表创建脚本.sql

```sql
DROP DATABASE  IF EXISTS `SPRING_SESSION`;
CREATE DATABASE `SPRING_SESSION` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `SPRING_SESSION`;

DROP TABLE IF EXISTS SPRING_SESSION_ATTRIBUTES;
DROP TABLE IF EXISTS SPRING_SESSION;
CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=INNODB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=INNODB ROW_FORMAT=DYNAMIC;
```

参考:

https://segmentfault.com/a/1190000039660173

https://www.cnblogs.com/l199616j/p/11195667.html
