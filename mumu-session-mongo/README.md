# mumu-session-mongo
***使用mongo文档数据库来存储session。***

**maven**
```
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-mongodb</artifactId>
    <version>1.9.4.RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-mongodb</artifactId>
    <version>4.1.4</version>
</dependency>        
```
**配置集成**
```
<!-- mongo客户端 -->
<bean id="mongo" class="com.mongodb.Mongo">
	<constructor-arg name="host" value="${mongo.host}"/>
	<constructor-arg name="port" value="${mongo.port}"/>
</bean>
<!-- mongo模板 -->
<bean id="mongoTemplate" class="org.springframework.data.mongodb.core.MongoTemplate">
	<constructor-arg name="mongo" ref="mongo"/>
	<constructor-arg name="databaseName" value="${mongo.databaseName}"/>
</bean>
<!-- 将session放入mongo -->
<bean id="mongoHttpSessionConfiguration"
	class="org.springframework.session.data.mongo.config.annotation.web.http.MongoHttpSessionConfiguration">
	<property name="maxInactiveIntervalInSeconds" value="${mongo.maxInactiveIntervalInSeconds}" />
	<property name="collectionName" value="${mongo.collectionName}" />
</bean>
```
**代码集成**
```
@EnableMongoHttpSession(maxInactiveIntervalInSeconds=3600,collectionName="springSessions")
public class MongoConfig {
	@Value("#{configProperties['mongo.host']}")
	private String host;
	@Value("#{configProperties['mongo.port']}")
	private int port;
	@Bean
	public MongoOperations mongoOperations() throws UnknownHostException {
		return new MongoTemplate(new MongoClient(host,port), "test");
	}
}
```

## 相关阅读  
[spring-session官网](http://projects.spring.io/spring-session/)   
## 联系方式
**以上观点纯属个人看法，如有不同，欢迎指正。  
email:<babymm@aliyun.com>  
github:[https://github.com/babymm](https://github.com/babymm)**
