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

# keytool -genkeypair -alias oauth2-auth-key -keyalg RSA -keypass sunny123 -keystore /usr/local/jks/oauth2.keystore -storepass zhy123
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

## 迁移到 PKCS12
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
keytool -importkeystore -srckeystore /usr/local/jks/oauth2.keystore -destkeystore /usr/local/jks/oauth2.keystore -deststoretype pkcs12
```

