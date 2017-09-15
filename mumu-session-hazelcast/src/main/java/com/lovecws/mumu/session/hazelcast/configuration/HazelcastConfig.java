package com.lovecws.mumu.session.hazelcast.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

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
