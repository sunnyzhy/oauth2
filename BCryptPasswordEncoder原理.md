# BCryptPasswordEncoder 原理

## 添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 示例

```java
@Test
void password() {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String password = "123";

    String encode1 = passwordEncoder.encode(password);
    System.out.println("encode1:" + encode1);
    boolean matches1 = passwordEncoder.matches(password, encode1);
    System.out.println("matches1:" + matches1);

    String encode2 = passwordEncoder.encode(password);
    System.out.println("encode2:" + encode2);
    boolean matches2 = passwordEncoder.matches(password, encode2);
    System.out.println("matches2:" + matches2);
}
```

输出:

```
encode1:$2a$10$9S5UcbY6ROF/jr79z..7GezNr8UeS05az93HB6CubYzTLmFVS8UoK
matches1:true
encode2:$2a$10$j3syVZY2dfMEHtoMox0Tauj9UGknhwFjHtVLylHCWubSohIgdkLqC
matches2:true
```

同一个明文加密两次，却输出了两上不同的密文。下面介绍其原理。

## encode 函数的原理

encode 函数入口:

```java
// BCryptPasswordEncoder.class
public String encode(CharSequence rawPassword) {
    if (rawPassword == null) {
        throw new IllegalArgumentException("rawPassword cannot be null");
    } else {
        String salt = this.getSalt();
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }
}
```

参数说明:

- ```CharSequence rawPassword```: 密码的明文，此处值为: ```123```

关键代码解析:

1. ```String salt = this.getSalt();```: 生成随机盐，此处值为: ```$2a$10$/jD5osy0uAjfh.JK5RZKbe```

依次断点进入:

```java
// BCrypt.class
public static String hashpw(String password, String salt) {
    byte[] passwordb = password.getBytes(StandardCharsets.UTF_8);
    return hashpw(passwordb, salt);
}
```

参数说明:

- ```String password```: 密码的明文，此处值为: ```123```
- ```String salt```: 随机盐，此处值为: ```$2a$10$/jD5osy0uAjfh.JK5RZKbe```

```java
// BCrypt.class
public static String hashpw(byte[] passwordb, String salt) {
    // ...
    
    String real_salt = salt.substring(off + 3, off + 25);
    byte[] saltb = decode_base64(real_salt, 16);
    
    // ...
    
    byte[] hashed = B.crypt_raw(passwordb, saltb, rounds, minor == 'x', minor == 'a' ? 65536 : 0);
    
    // ...
}
```

参数说明:

- ```byte[] passwordb```: 密码的明文，此处值为: ```[49, 50, 51]```
- ```String salt```: 随机盐，此处值为: ```$2a$10$/jD5osy0uAjfh.JK5RZKbe```

关键代码解析:

1. ```String real_salt = salt.substring(off + 3, off + 25);```: 一般情况下，从随机盐的第 ```7``` 位开始，取 ```22``` 个长度 ```( off + 25 - off - 3 )``` 的字符串，得到真实的盐，此处值为: ```/jD5osy0uAjfh.JK5RZKbe```
2. ```byte[] saltb = decode_base64(real_salt, 16);```: 使用 ```base64``` 对 ```real_salt``` 进行解码
3. ```byte[] hashed = B.crypt_raw(passwordb, saltb, rounds, minor == 'x', minor == 'a' ? 65536 : 0);```: 对密码的明文 ```123``` 和随机盐 ```/jD5osy0uAjfh.JK5RZKbe``` 进行加密处理

## matches 函数的原理

matches 函数入口:

```java
// BCryptPasswordEncoder.class
public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (rawPassword == null) {
        throw new IllegalArgumentException("rawPassword cannot be null");
    } else if (encodedPassword != null && encodedPassword.length() != 0) {
        if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            this.logger.warn("Encoded password does not look like BCrypt");
            return false;
        } else {
            return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
        }
    } else {
        this.logger.warn("Empty encoded password");
        return false;
    }
}
```

参数说明:

- ```CharSequence rawPassword```: 密码的明文: 此处值为: ```123```
- ```String encodedPassword```: 密码的密文: 此处值为: ```$2a$10$/jD5osy0uAjfh.JK5RZKben3W7fj084SQDDWVk/g.sR3uvS9.plhG```

依次断点进入:

```java
// BCrypt.class
public static boolean checkpw(String plaintext, String hashed) {
    return equalsNoEarlyReturn(hashed, hashpw(plaintext, hashed));
}
```

参数说明:

- ```String plaintext```: 密码的明文: 此处值为: ```123```
- ```String hashed```: 密码的密文: 此处值为: ```$2a$10$/jD5osy0uAjfh.JK5RZKben3W7fj084SQDDWVk/g.sR3uvS9.plhG```

```java
// BCrypt.class
public static String hashpw(String password, String salt) {
    byte[] passwordb = password.getBytes(StandardCharsets.UTF_8);
    return hashpw(passwordb, salt);
}
```

参数说明:

- ```String password```: 密码的明文: 此处值为: ```123```
- ```String salt```: 密码的密文，此处值为: ```$2a$10$/jD5osy0uAjfh.JK5RZKben3W7fj084SQDDWVk/g.sR3uvS9.plhG```
   - ***注: 此密文包含了随机盐，下面会讲到***

```java
// BCrypt.class
public static String hashpw(byte[] passwordb, String salt) {
    // ...
    
    String real_salt = salt.substring(off + 3, off + 25);
    byte[] saltb = decode_base64(real_salt, 16);
    
    // ...
    
    byte[] hashed = B.crypt_raw(passwordb, saltb, rounds, minor == 'x', minor == 'a' ? 65536 : 0);
    
    // ...
}
```

参数说明:

- ```byte[] passwordb```: 密码的明文，此处值为: ```[49, 50, 51]```
- ```String salt```: 随机盐，此处值为: ```$2a$10$/jD5osy0uAjfh.JK5RZKben3W7fj084SQDDWVk/g.sR3uvS9.plhG```

关键代码解析:

1. ```String real_salt = salt.substring(off + 3, off + 25);```: 一般情况下，从随机盐的第 ```7``` 位开始，取 ```22``` 个长度 ```( off + 25 - off - 3 )``` 的字符串，得到真实的盐，此处值为: ```/jD5osy0uAjfh.JK5RZKbe```
2. ```byte[] saltb = decode_base64(real_salt, 16);```: 使用 ```base64``` 对 ```real_salt``` 进行解码
3. ```byte[] hashed = B.crypt_raw(passwordb, saltb, rounds, minor == 'x', minor == 'a' ? 65536 : 0);```: 对密码的明文 ```123``` 和随机盐 ```/jD5osy0uAjfh.JK5RZKbe``` 进行加密处理
4. 流程同 ```encode 函数的原理``` 的最后一部分，所以得出的密文也跟 ```encode``` 加密所得出的密文一样。

## 随机盐

1. 随机盐的长度
   - ```getSalt()``` 生成的随机盐，长度为 ```29```
   - ```BCryptPasswordEncoder.encode()``` 生成的随机盐，长度为 ```60```
2. ***一般情况下***:
   - 随机盐的起始字符串固定为 ```$2a$10$```
   - 真实盐是从随机盐的第 ```7``` 位开始，取 ```22``` 个长度( off + 25 - off - 3 )的字符串
