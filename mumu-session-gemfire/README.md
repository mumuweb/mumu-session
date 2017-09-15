# mumu-session-gemfire
***使用gemfire来存储session***  
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
## 相关阅读  
[spring-session官网](http://projects.spring.io/spring-session/)   
## 联系方式
**以上观点纯属个人看法，如有不同，欢迎指正。  
email:<babymm@aliyun.com>  
github:[https://github.com/babymm](https://github.com/babymm)**