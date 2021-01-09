# JKS
JKS是Java密钥库(Java KeyStore)，即 Java 用来存储密钥的容器。可以同时容纳 n 个公钥或私钥，后缀一般是 .jks 或者 .keystore 或.truststore 等。

## 1. 生成密钥对
命令行格式 :
```bash
# keytool -genkeypair [OPTION]...

Generates a key pair

Options:

 -alias <alias>                  alias name of the entry to process (要处理的条目的别名)
 -keyalg <keyalg>                key algorithm name (密钥算法名称)
 -keysize <keysize>              key bit size (密钥位大小)
 -sigalg <sigalg>                signature algorithm name (签名算法名称)
 -destalias <destalias>          destination alias (目标别名)
 -dname <dname>                  distinguished name (唯一判别名)
 -startdate <startdate>          certificate validity start date/time (证书有效期开始日期/时间)
 -ext <value>                    X.509 extension (X.509 扩展)
 -validity <valDays>             validity number of days (有效天数)
 -keypass <arg>                  key password ( 密钥口令)
 -keystore <keystore>            keystore name (密钥库名称)
 -storepass <arg>                keystore password (密钥库口令)
 -storetype <storetype>          keystore type (密钥库类型)
 -providername <providername>    provider name (提供方名称)
 -providerclass <providerclass>  provider class name (提供方类名)
 -providerarg <arg>              provider argument (提供方参数)
 -providerpath <pathlist>        provider classpath (提供方类路径)
 -v                              verbose output (详细输出)
 -protected                      password through protected mechanism (通过受保护的机制的口令)
```

命令实例 :
```bash
# mkdir -p /usr/local/jks

# keytool -genkeypair -alias oauth2-auth-key -keyalg RSA -keypass zhy123 -keystore /usr/local/jks/oauth2.keystore -storepass zhy123
What is your first and last name?
  [Unknown]:  zhy
What is the name of your organizational unit?
  [Unknown]:  zhy
What is the name of your organization?
  [Unknown]:  zhy
What is the name of your City or Locality?
  [Unknown]:  zhy
What is the name of your State or Province?
  [Unknown]:  zhy          
What is the two-letter country code for this unit?
  [Unknown]:  zhy
Is CN=zhy, OU=zhy, O=zhy, L=zhy, ST=zhy, C=zhy correct?
  [no]:  y


Warning:
The JKS keystore uses a proprietary format. It is recommended to migrate to PKCS12 which is an industry standard format using "keytool -importkeystore -srckeystore /usr/local/jks/oauth2.keystore -destkeystore /usr/local/jks/oauth2.keystore -deststoretype pkcs12".

# ls /usr/local/jks
oauth2.keystore
```

出现了一条警告信息，接着执行下面的操作。

## 从其他密钥库导入一个或所有条目
命令行格式 :
```bash
# keytool -importkeystore [OPTION]...

Imports one or all entries from another keystore

Options:

 -srckeystore <srckeystore>            source keystore name （源密钥库名称）
 -destkeystore <destkeystore>          destination keystore name （目标密钥库名称）
 -srcstoretype <srcstoretype>          source keystore type （源密钥库类型）
 -deststoretype <deststoretype>        destination keystore type （目标密钥库类型）
 -srcstorepass <arg>                   source keystore password （源密钥库口令）
 -deststorepass <arg>                  destination keystore password （目标密钥库口令）
 -srcprotected                         source keystore password protected （受保护的源密钥库口令）
 -srcprovidername <srcprovidername>    source keystore provider name （源密钥库提供方名称）
 -destprovidername <destprovidername>  destination keystore provider name （目标密钥库提供方名称）
 -srcalias <srcalias>                  source alias （源别名）
 -destalias <destalias>                destination alias （目标别名）
 -srckeypass <arg>                     source key password （源密钥口令）
 -destkeypass <arg>                    destination key password （目标密钥口令）
 -noprompt                             do not prompt （不提示）
 -providerclass <providerclass>        provider class name （提供方类名）
 -providerarg <arg>                    provider argument （提供方参数）
 -providerpath <pathlist>              provider classpath （提供方类路径）
 -v                                    verbose output （详细输出）
```

命令实例 :
```bash
# keytool -importkeystore -srckeystore /usr/local/jks/oauth2.keystore -destkeystore /usr/local/jks/oauth2.keystore -deststoretype pkcs12
Enter source keystore password:  zhy123
Entry for alias oauth2-auth-key successfully imported.
Import command completed:  1 entries successfully imported, 0 entries failed or cancelled

Warning:
Migrated "/usr/local/jks/oauth2.keystore" to Non JKS/JCEKS. The JKS keystore is backed up as "/usr/local/jks/oauth2.keystore.old".

# ls
oauth2.keystore  oauth2.keystore.old
```

## 获取公钥
```bash
# keytool -list -rfc --keystore /usr/local/jks/oauth2.keystore | openssl x509 -inform pem -pubkey
Enter keystore password:  zhy123
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiYR2i8qRo0xlT/v4slKH
UMlMqpwSAGWZX9J11YXRj1144qfKit6rVXjeEKDQDiiGQvxL4tZe5PunsoPFq5vJ
Tvz58GpJPijnf9WkuD4Po7SPiFfJEpJNz/XEgC9Y3Vqo9Q/+XYdiTNgFo0F+halK
iwI0XYzM6Bd+fGpXXFBPTdx0Dka9ZqRyl7KoR09SX1Tg6D7bdOqk/oBM3UMTs7B8
MIhvo37wdHqliBX7xEVRRRY1jrgpy1w4HJPFkRftE4Q9MaARBQA13WS1lkpIpdN0
1y+snWynTYmLQRxLj4Yd6x1BJOnIVAfO1AVrF7EzdjHPQzuRA0jRs2//LRlgFHQ+
qQIDAQAB
-----END PUBLIC KEY-----
-----BEGIN CERTIFICATE-----
MIIDRzCCAi+gAwIBAgIEHwhI4DANBgkqhkiG9w0BAQsFADBUMQwwCgYDVQQGEwN6
aHkxDDAKBgNVBAgTA3poeTEMMAoGA1UEBxMDemh5MQwwCgYDVQQKEwN6aHkxDDAK
BgNVBAsTA3poeTEMMAoGA1UEAxMDemh5MB4XDTIxMDEwOTA2NTY1NloXDTIxMDQw
OTA2NTY1NlowVDEMMAoGA1UEBhMDemh5MQwwCgYDVQQIEwN6aHkxDDAKBgNVBAcT
A3poeTEMMAoGA1UEChMDemh5MQwwCgYDVQQLEwN6aHkxDDAKBgNVBAMTA3poeTCC
ASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAImEdovKkaNMZU/7+LJSh1DJ
TKqcEgBlmV/SddWF0Y9deOKnyoreq1V43hCg0A4ohkL8S+LWXuT7p7KDxaubyU78
+fBqST4o53/VpLg+D6O0j4hXyRKSTc/1xIAvWN1aqPUP/l2HYkzYBaNBfoWpSosC
NF2MzOgXfnxqV1xQT03cdA5GvWakcpeyqEdPUl9U4Og+23TqpP6ATN1DE7OwfDCI
b6N+8HR6pYgV+8RFUUUWNY64KctcOByTxZEX7ROEPTGgEQUANd1ktZZKSKXTdNcv
rJ1sp02Ji0EcS4+GHesdQSTpyFQHztQFaxexM3Yxz0M7kQNI0bNv/y0ZYBR0PqkC
AwEAAaMhMB8wHQYDVR0OBBYEFFLUxiknzDpMaAXcQfFOqz4LwsyDMA0GCSqGSIb3
DQEBCwUAA4IBAQAmK1T9puPgzWvd3OL3TKGN9KTFh9uzrZQ7VfiQ/w4vvLHpsQwB
2xRIGcgmJX/HJPjKAvqIL3cRoFBLKu9B9F/sdJyz/1gN+rWfzeyvIRyOuIQ/nsh6
xBc8Cm0sLcw0qgUAGOvU92Ypf02o9PbXdOkuTjHS96XzrQm1OZphFKW5zw5WOgo5
TOstKHyrfmeaWQhSU0Mr5A8pJdmw86hSrvnPS//82xb6lUKjhlLLK9DDkPyNHVsZ
PvRZiPeII7K8wQgt4W+tyEzszxjIreHGdXQZttSLqFV51ZWjYS9yv3Xjl0jRU9O4
UcYjgNODtwIA8swIY/yRT2CKXUFkKMr3hQuw
-----END CERTIFICATE-----
```

输出的内容包含了公钥的信息，也就是从 BEGIN PUBLIC KEY 到 END PUBLIC KEY 之间的字符串，该字符串用于校验签名（私钥加密后的内容）。

