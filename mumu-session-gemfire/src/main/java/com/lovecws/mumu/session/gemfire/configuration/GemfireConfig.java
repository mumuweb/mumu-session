package com.lovecws.mumu.session.gemfire.configuration;

import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.GemfireOperations;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;

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
