# DERROR

# 背景
早期，由于线上环境监控简单，只要一出现error日志就会呲呲呲的报警。有的时候我们的一些业务场景并不需要报警，报警次数需要一定的限制。那么derror就是您最好的选择。

# 项目介绍
名称：derror 

译意： 对坑错误

语言： 纯java开发

定位： error日志报警，支持钉钉，邮件，提供SPI接口可供扩展。

# 快速开始
>  * JDK 1.6 or above
>  * 编译工具 [Maven][maven]

## 添加依赖
   如果是logback,添加如下:
   ```xml
    <dependency>
      <groupId>org.jfaster.derror</groupId>
      <artifactId>derror-logback-plugin</artifactId>
      <version>1.0.0</version>
    </dependency>
   ```
### logback的配置
- logback基本配置
```xml
	<appender name="errorLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
			<level>ERROR</level>
		</filter>
		<File>log/derror.error.log</File>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<FileNamePattern>log/history/%d{yyyyMM,aux}/derror.error.log.%d{yyyyMMdd}</FileNamePattern>
			<maxHistory>30</maxHistory>
		</rollingPolicy>
		<encoder>
			<charset>UTF-8</charset>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{15} %X{traceId} - %msg%n</pattern>
		</encoder>
		<filter class="org.jfaster.derror.logback.plugin.filter.LogbackErrorFilter">
			<level>ERROR</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
			<env>test</env>
			<appName>dispatcher</appName>
			<token>1234</token>
			<traceKey>traceId</traceKey>
			<url>http://localhost:8080</url>
		</filter>
	</appender>
```
## logback的配置说明
   * env: 环境标识
   * appName: DERROR后台配置的app名称
   * token: DERROR后台生成的token值
   * traceKey: 用于跟踪日志的traceId
   * url: DERROR后台的地址
   * queueSize: 队列大小,默认256
   * remoteInterval: 轮询ERROR后台时间间隔默认10s, 单位s
   * errorSwitch: 开关,false表示不启用报警 true表示启用报警
## derror报警扩展
   * classpath下创建META-INF/alarms目录并新建org.jfaster.derror.alarm.Alarm文件
   * 实现接口org.jfaster.derror.alarm
# 后台管理界面
   > DERROR 提供可视化的后台管理，可以配置报警频次等。
   
# 贡献者

* liangyanghe([@liangyanghe](https://github.com/jfaster/mango))
* yangnan([@yangnan](https://github.com/lanmo/derror))
* wangxiaozhong([@wangxiaozhong](https://github.com/amoswxz))

[maven]:https://maven.apache.org
[gradle]:http://gradle.org
