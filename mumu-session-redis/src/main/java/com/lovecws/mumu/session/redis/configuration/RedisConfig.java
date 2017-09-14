package com.lovecws.mumu.session.redis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

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
