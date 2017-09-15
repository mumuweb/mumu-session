# mumu-session session共享
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/mumudemo/mumu-kafka/blob/master/LICENSE) 
[![Maven Central](https://img.shields.io/maven-central/v/com.weibo/motan.svg?label=Maven%20Central)](https://github.com/mumuweb/mumu-session) 
[![Build Status](https://travis-ci.org/mumuweb/mumu-session.svg?branch=master)](https://travis-ci.org/mumuweb/mumu-session)
[![codecov](https://codecov.io/gh/mumuweb/mumu-session/branch/master/graph/badge.svg)](https://codecov.io/gh/mumuweb/mumu-session)
[![OpenTracing-1.0 Badge](https://img.shields.io/badge/OpenTracing--1.0-enabled-blue.svg)](http://opentracing.io)

***spring session可以管理web项目创建的HttpSession。当用户打开浏览器浏览的时候在web服务器就会创建HttpSession，保存着浏览器数据和服务器之间的凭证信息（登录认证信息）。当单台部署web项目的时候，servlet自带的HttpSession完全够用，但是随着项目越做越大，项目需提供更好的可利用性和并发量，就需要将项目部署到多个机器而形成集群（[nginx](https://nginx.org/)反向代理、[squid](http://www.squid-cache.org/)缓存等），但是这时候就会出现session共享问题，因为session只保存在服务器内存上，所以需要spring-session来同意管理项目的session，将session保存在缓存服务器上（[redis](https://redis.io/)、[mongo](https://www.mongodb.com/)、[gemfire](https://pivotal.io/pivotal-gemfire)、[hazelcast](https://hazelcast.org/)等），从而实现项目集群。***

## 简介
**Spring Session provides the following features:**
-  API and implementations for managing a user's session
-  HttpSession - allows replacing the HttpSession in an application container (i.e. Tomcat) neutral way
-  Clustered Sessions - Spring Session makes it trivial to support clustered sessions without being tied to an application container specific solution.
-  Multiple Browser Sessions - Spring Session supports managing multiple users' sessions in a single browser instance (i.e. multiple authenticated accounts similar to Google).
-  RESTful APIs - Spring Session allows providing session ids in headers to work with RESTful APIs
-  WebSocket - provides the ability to keep the HttpSession alive when receiving -  WebSocket messages

## spring-session原理
![spring-session机构图](http://img.blog.csdn.net/20170316154920745?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd29qaWFvbGluYWFh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## redis集成
使用redis来缓存session。  
**maven**
```
<!-- spring-data-redis -->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <version>1.8.7.RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.4.1</version>
</dependency>
```
**配置集成**
```
<!-- redis配置信息 -->
<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
</bean>
<!-- 创建redis连接工厂 -->
<bean id="jedisConnectionFactory"
     class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
    <property name="hostName" value="${jedis.host}" />
    <property name="port" value="${jedis.port}" />
    <property name="password" value="${jedis.password}" />
    <property name="poolConfig" ref="jedisPoolConfig" />
    <property name="usePool" value="true" />
</bean>
<!-- 创建redis模板 -->
    <bean id="redisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate">
        <property name="connectionFactory" ref="jedisConnectionFactory" />
    </bean>
<!-- 将session放入redis -->
<bean id="redisHttpSessionConfiguration"
    class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration">
    <property name="maxInactiveIntervalInSeconds" value="1800" />
</bean>	
```
**代码集成**
```
/**
 * 通过spring配置的方式 获取redis连接池
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=7200,redisFlushMode=RedisFlushMode.ON_SAVE,redisNamespace="mumu.spring.session")
public class RedisConfig {
    @Value("#{configProperties['jedis.host']}")
    private String host;
    @Value("#{configProperties['jedis.port']}")
    private int port;
    @Value("#{configProperties['jedis.password']}")
    private String password;
    @Bean
    public RedisConnectionFactory connectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setPassword(password);
        return jedisConnectionFactory; 
    }
}
```
## mongo 集成
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
## gemfire 集成
**maven**
```
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-gemfire</artifactId>
    <version>1.8.10.RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.gemstone.gemfire</groupId>
    <artifactId>gemfire</artifactId>
    <version>8.2.0</version>
</dependency>
```
**配置集成**
```
<!-- gemfire客户端 -->
<bean id="adminRegion" class="com.gemstone.gemfire.internal.admin.remote.AdminRegion">
	<property name="globalName" value=""></property>
	<property name="localName" value=""></property>
	<property name="globalName" value=""></property>
</bean>
<!-- gemfire模板 -->
<bean id="sessionRegionTemplate"            class="org.springframework.data.gemfire.GemfireTemplate"></bean>
<!-- 将session放入gemfire -->
<bean id="gemFireHttpSessionConfiguration"
	class="org.springframework.session.data.gemfire.config.annotation.web.http.GemFireHttpSessionConfiguration">
    <property name="maxInactiveIntervalInSeconds" value="1800" />
</bean>
```
**代码集成**
```
@EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 1800)
public class GemfireConfig {
    @Bean
	public Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();
		gemfireProperties.setProperty("name", "ExampleClient");
		gemfireProperties.setProperty("log-level", "warning");
		return gemfireProperties;
	}
	@Bean
	public CacheFactoryBean gemfireCache() throws Exception {
		CacheFactoryBean cache = new CacheFactoryBean();
		cache.setProperties(gemfireProperties());
		return cache;
	}
	@Bean
	public LocalRegionFactoryBean<String, String> region() {
		LocalRegionFactoryBean<String, String> helloRegion = new LocalRegionFactoryBean<>();
		try {
			helloRegion.setCache(gemfireCache().getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		helloRegion.setClose(false);
		helloRegion.setName("hello");
		helloRegion.setPersistent(false);
		return helloRegion;
	}
	@Bean
	public GemfireOperations gemfireOperations() throws UnknownHostException {
		try {
			return new GemfireTemplate(region().getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
```
## hazelcast 集成
**maven**
```
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
    <version>3.6.5</version>
</dependency>
```
**配置集成**
```
<!-- 创建hazecast实例-->
<bean id="hazelcastInstance" class="com.hazelcast.instance.HazelcastInstanceImpl">
    <constructor-arg name="name" value="hazelcastInstance"></constructor-arg>
    <constructor-arg name="config">
        <bean id="hazelcastInstance" class="com.hazelcast.config.Config"/>
    </constructor-arg>
    <constructor-arg name="nodeContext">
        <bean class="com.hazelcast.instance.DefaultNodeContext"/>
    </constructor-arg>
</bean>
<!-- 将session放入hazelcast -->
<bean id="hazelcastHttpSessionConfiguration"
    class="org.springframework.session.hazelcast.config.annotation.web.http.HazelcastHttpSessionConfiguration">
    <property name="maxInactiveIntervalInSeconds" value="1800"/>
    <property name="sessionMapName" value="mumu:session:hazelcast"/>
</bean>
```
**代码集成**
```
/**
* 通过spring配置的方式 获取hazelcast
*/
@EnableHazelcastHttpSession(sessionMapName = "mumu:session:hazelcast")
public class HazelcastConfig {
    /**
        * 嵌入式 集成hazelcast
        *
        * @return
        */
    @Bean
    public HazelcastInstance embeddedHazelcast() {
        Config hazelcastConfig = new Config();
        return Hazelcast.newHazelcastInstance(hazelcastConfig);
    }
}
```
## 相关阅读  
[spring-session官网](http://projects.spring.io/spring-session/)   
[hazelcast的坑爹事](http://blog.csdn.net/hengyunabc/article/details/18514563)  
## 联系方式
**以上观点纯属个人看法，如有不同，欢迎指正。  
email:<babymm@aliyun.com>  
github:[https://github.com/babymm](https://github.com/babymm)**