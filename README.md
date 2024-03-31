# qwrobotnotice
对特定注解拦截，可自定义消息通知企微机器人
For specific annotation interception, you can customize the message to notify the enterprise micro robot;

# 中文
## 食用方法
### 1、在项目启动配置文件配置
properties文件
```properties
qw.notice.env=dev
qw.notice.robotKey=xxxxxxxxx-xxxx-xxxx-xxxxxxxx
qw.notice.traceIdName=traceId
```
或者yaml文件
```yaml
qw:
  notice:
    env: dev
    robotKey: xxxxxxxxx-xxxx-xxxx-xxxxxxxx
    traceIdName: traceId
```
### 2、在SpringBoot启动类添加注解
```java
@SpringBootApplication
@QwTemplateScan
public class XXXApplication {

}
```
### 3、启动Application!开始玩耍把
Run Application

## 原理
注入配置文件[QwNoticeConfig](src%2Fmain%2Fjava%2Fcom%2Fcnby%2Fqw%2Fconfig%2FQwNoticeConfig.java)
使用SpringBoot-Aop拦截自定义注解[QwNotification](src%2Fmain%2Fjava%2Fcom%2Fcnby%2Fqw%2Faop%2FQwNotification.java)

# English
## Eating method(How to use it)
### 1、Configure in the project startup configuration file
.properties file
```properties
qw.notice.env=dev
qw.notice.robotKey=xxxxxxxxx-xxxx-xxxx-xxxxxxxx
qw.notice.traceIdName=traceId
```
or .yaml file
```yaml
qw:
  notice:
    env: dev
    robotKey: xxxxxxxxx-xxxx-xxxx-xxxxxxxx
    traceIdName: traceId
```
### 2、Add annotation to the Spring Boot startup class
```java
@SpringBootApplication
@QwTemplateScan
public class XXXApplication {

}
```
### 3、run application!Start playing!
Run Application

## principle
Injection profile[QwNoticeConfig](src%2Fmain%2Fjava%2Fcom%2Fcnby%2Fqw%2Fconfig%2FQwNoticeConfig.java)
Use Spring Boot-Aop to block custom annotation[QwNotification](src%2Fmain%2Fjava%2Fcom%2Fcnby%2Fqw%2Faop%2FQwNotification.java)
