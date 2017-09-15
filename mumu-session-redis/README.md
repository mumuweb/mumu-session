# mumu-session-redis 
***使用redis作为存储HttpSession的容器，当session创建的时候，在redis中创建一个key，值是该session的序列化对象，并且可以设置过期时间为sessionTimeOut。***

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
## 相关阅读  
[spring-session官网](http://projects.spring.io/spring-session/)   
## 联系方式
**以上观点纯属个人看法，如有不同，欢迎指正。  
email:<babymm@aliyun.com>  
github:[https://github.com/babymm](https://github.com/babymm)**