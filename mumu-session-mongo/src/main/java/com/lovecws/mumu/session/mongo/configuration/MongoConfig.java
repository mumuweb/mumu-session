package com.lovecws.mumu.session.mongo.configuration;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

//@EnableMongoHttpSession(maxInactiveIntervalInSeconds=3600,collectionName="springSessions")
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
