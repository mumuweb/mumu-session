# mumu-session-hazelcat
***使用hazelcat来存储session***  
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
